package com.example.wineapp.model;

import android.graphics.Bitmap;

import com.example.wineapp.MyApplication;

import java.util.LinkedList;
import java.util.List;

public class Model {
    static final public Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();
    private  Model(){}
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
    public interface GetAllUserListener{
        void onComplete(List<User> data);
    }
    public void getAllUser(GetAllUserListener listener){
        modelFirebase.getAllUser(listener);
//        MyApplication.executorService.execute(()->{
//            List<User> data = AppLocalDB.db.userDao().getAll();
//            MyApplication.mainHandler.post(()->{
//                listener.onComplete(data);
//            });
//        });
    }

    public interface AddUserListener{
        void onComplete();
    }
    public void addUser(User user, AddUserListener listener){
       modelFirebase.addUser(user,listener);
//        MyApplication.executorService.execute(()->{
//            AppLocalDB.db.userDao().insertAll(user);
//            MyApplication.mainHandler.post(()->{
//                listener.onComplete();
//            });
//        });
    }

    public interface GetUserByNameListener{
        void onComplete(User user);
    }
    public void getUserByName(String userName,GetUserByNameListener listener) {
        modelFirebase.getUserByName(userName,listener);
//        MyApplication.executorService.execute(()->{
//            User user = AppLocalDB.db.userDao().getUserByName(userName);
//            MyApplication.mainHandler.post(()->{
//                listener.onComplete(user);
//            });
//        });
    }
    public interface DeleteUserListener{
        void onComplete();
    }
    public void DeleteUser(User user, DeleteUserListener listener){
        modelFirebase.deleteUser(user,listener);
//        MyApplication.executorService.execute(()->{
//            AppLocalDB.db.userDao().delete(user);
//            MyApplication.mainHandler.post(()->{
//                listener.onComplete();
//            });
//        });
    }
    public interface UploadImageListener{
        public void onComplete(String url);
    }
    public void uploadImage(Bitmap bitmap, String name, final UploadImageListener listener){
        modelFirebase.uploadImage(bitmap,name,listener);
    }
}
