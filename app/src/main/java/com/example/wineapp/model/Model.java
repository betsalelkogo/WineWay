package com.example.wineapp.model;

import com.example.wineapp.MyApplication;

import java.util.LinkedList;
import java.util.List;

public class Model {
    static final public Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();
    private List<User> users = new LinkedList<User>();
    private  Model(){
        User u=new User("1","1","1");
        User u2=new User("2","2","2");
        users.add(u);
        users.add(u2);
    }
    public List<User> getUsersList(){
        return users;
    }
    public int getUserPosition(String name) {
        for(int i=0;i<users.size();i++)
            if(name.equals(users.get(i).getName()))
                return i;
        return 0;
    }
//////////////////////////////////////////////////////////////////////////////////
    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }
    public void getAllPosts(GetAllPostsListener listener){
        modelFirebase.getAllPosts(listener);
//        MyApplication.executorService.execute(()->{
//            List<Post> data = AppLocalDB.db.postDao().getAll();
//            MyApplication.mainHandler.post(()->{
//                listener.onComplete(data);
//            });
//        });
    }

    public interface AddPostListener{
        void onComplete();
    }
    public void addPost(Post post, AddPostListener listener){
        modelFirebase.addPost(post,listener);
//        MyApplication.executorService.execute(()->{
//            AppLocalDB.db.postDao().insertAll(post);
//            MyApplication.mainHandler.post(()->{
//                listener.onComplete();
//            });
//        });
    }

    public interface GetPostByNameListener{
        void onComplete(Post post);
    }
    public void getPostByName(String postId,GetPostByNameListener listener) {
        modelFirebase.getPostByName(postId,listener);
//        MyApplication.executorService.execute(()->{
//            Post post = AppLocalDB.db.postDao().getPostByName(postId);
//            MyApplication.mainHandler.post(()->{
//                listener.onComplete(post);
//            });
//        });
    }
    public interface DeletePostListener{
        void onComplete();
    }
    public void DeletePost(Post post, DeletePostListener listener){
        modelFirebase.deletePost(post,listener);
//        MyApplication.executorService.execute(()->{
//            AppLocalDB.db.postDao().delete(post);
//            MyApplication.mainHandler.post(()->{
//                listener.onComplete();
//            });
//        });
    }
}
