package com.example.wineapp.model;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wineapp.MyApplication;

import java.util.List;

public class Model {
    static final public Model instance = new Model();
    MutableLiveData<List<Post>> postsListLd = new MutableLiveData<List<Post>>();
    ModelFirebase modelFirebase = new ModelFirebase();
    private  Model(){
        reloadPostsList();
    }
    public LiveData<List<Post>> getAll() {
        return postsListLd;
    }
    public void reloadPostsList() {
        //1. get local last update
        Long localLastUpdate = Post.getLocalLastUpdated();
        //2. get all students record since local last update from firebase
        modelFirebase.getAllPosts(localLastUpdate,(list)->{
            if(list !=null){
                MyApplication.executorService.execute(()->{
                    //3. update local last update date
                    //4. add new records to the local db
                    Long lLastUpdate = new Long(0);
                    for(Post p : list){
                        if(!p.isDeleted()) {
                            AppLocalDB.db.postDao().insertAll(p);
                        }
                        else {
                            AppLocalDB.db.postDao().delete(p);
                        }
                        if (p.getLastUpdated() > lLastUpdate){
                            lLastUpdate = p.getLastUpdated();
                        }
                    }
                    Post.setLocalLastUpdated(lLastUpdate);
                    //5. return all records to the caller
                    List<Post> stList = AppLocalDB.db.postDao().getAll();
                    for (Post p: stList){
                        if(p.isDeleted()){
                            AppLocalDB.db.postDao().delete(p);
                        }
                    }
                    postsListLd.postValue(stList);
                });
            }
        });
    }

    public void addPost(Post post, AddPostListener listener){
        modelFirebase.addPost(post,()->{
            reloadPostsList();
            listener.onComplete();
        });
    }

    public void addUser(User user, AddUserListener listener){
        modelFirebase.addUser(user, () ->{
            listener.onComplete();
        });

    }

    public void DeletePost(Post post, DeletePostListener listener){
        post.setDeleted(true);
        addPost(post,()->{
            listener.onComplete();
        });
    }

    public void getUserByEmail(String userEmail,GetUserByEmailListener listener) {
        modelFirebase.getUserByEmail(userEmail,listener);
//        MyApplication.executorService.execute(()->{
//            User user = AppLocalDB.db.userDao().getUserByEmail(userEmail);
//            MyApplication.mainHandler.post(()->{
//                listener.onComplete(user);
//            });
//        });
    }
    public void uploadImage(Bitmap bitmap, String name, final UploadImageListener listener){
        modelFirebase.uploadImage(bitmap,name,listener);
    }
    public interface UploadImageListener{
        void onComplete(String url);
    }
    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }
    public interface GetUserByEmailListener{
        void onComplete(User user);
    }
    public interface AddPostListener{
        void onComplete();
    }
    public interface AddUserListener{
        void onComplete();
    }
    public interface GetAllUserListener{
        void onComplete(List<User> data);
    }
    public interface GetPostByNameListener{
        void onComplete(Post post);
    }
    public interface DeletePostListener{
        void onComplete();
    }
}
