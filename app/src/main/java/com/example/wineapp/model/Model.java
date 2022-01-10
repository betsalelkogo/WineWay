package com.example.wineapp.model;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wineapp.MyApplication;
import com.example.wineapp.model.intefaces.AddPostListener;
import com.example.wineapp.model.intefaces.AddUserListener;
import com.example.wineapp.model.intefaces.DeletePostListener;
import com.example.wineapp.model.intefaces.GetUserByEmailListener;
import com.example.wineapp.model.intefaces.UploadImageListener;

import java.util.List;

public class Model {
    static final public Model instance = new Model();
    MutableLiveData<LoadingState> loadingState= new MutableLiveData<LoadingState>();
    public LiveData<LoadingState> getLoadingState(){return loadingState;}
    MutableLiveData<List<Post>> postsListLd = new MutableLiveData<List<Post>>();
    ModelFirebase modelFirebase = new ModelFirebase();
    private  Model(){
        loadingState.setValue(LoadingState.loaded);
        reloadPostsList();
    }
    public LiveData<List<Post>> getAll() {
        return postsListLd;
    }
    public void reloadPostsList() {
        loadingState.setValue(LoadingState.loading);
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
                    loadingState.postValue(LoadingState.loaded);
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

    public LiveData<List<Post>> getPostByName(String postName) {
        return AppLocalDB.db.postDao().getPostByName(postName);
    }
    public void DeletePost(Post post, DeletePostListener listener){
        post.setDeleted(true);
        addPost(post,()->{
            listener.onComplete();
        });
    }

    public void getUserByEmail(String userEmail, GetUserByEmailListener listener) {
        modelFirebase.getUserByEmail(userEmail,listener);
    }
    public void uploadImage(Bitmap bitmap, String name, final UploadImageListener listener){
        modelFirebase.uploadImage(bitmap,name,listener);
    }


}
