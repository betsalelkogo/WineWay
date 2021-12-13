package com.example.wineapp.model;

import com.example.wineapp.MyApplication;

import java.util.LinkedList;
import java.util.List;

public class Model {
    static final public Model instance = new Model();
    private List<User> users = new LinkedList<User>();
    private List<Post> data = new LinkedList<Post>();
    private  Model(){
        User u=new User("1","1","1");
        User u2=new User("2","2","2");
        users.add(u);
        users.add(u2);
    }
    public List<Post> getPostList(){
        return data;
    }
    public List<User> getUsersList(){
        return users;
    }
    public void addNewPost(Post post){
        data.add(post);
    }
    public void addNewUser(User user){
        users.add(user);
    }
    public Post getPost(String name) {
        return data.get(getPostPosition(name));
    }
    public User getUser(int i){return users.get(i);}
    private int getPostPosition(String name) {
        for(int i=0;i<data.size();i++)
            if(name.equals(data.get(i).getName()))
                return i;
        return 0;
    }
    public int getUserPosition(String name) {
        for(int i=0;i<users.size();i++)
            if(name.equals(users.get(i).getName()))
                return i;
        return 0;
    }
    public void DeletePostPosition(int postPosition) {
        data.remove(postPosition);
    }
    public void EditPost(Post post, int position) {
        data.set(position,post);
    }
//////////////////////////////////////////////////////////////////////////////////
    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }
    public void getAllPosts(GetAllPostsListener listener){
        MyApplication.executorService.execute(()->{
            List<Post> data = AppLocalDB.db.postDao().getAll();
            MyApplication.mainHandler.post(()->{
                listener.onComplete(data);
            });
        });
    }

    public interface AddPostListener{
        void onComplete();
    }
    public void addPost(Post post, AddPostListener listener){
        MyApplication.executorService.execute(()->{
            AppLocalDB.db.postDao().insertAll(post);
            MyApplication.mainHandler.post(()->{
                listener.onComplete();
            });
        });
    }

    public interface GetPostByNameListener{
        void onComplete(Post post);
    }
    public void getPostByName(String postId,GetPostByNameListener listener) {
        MyApplication.executorService.execute(()->{
            Post post = AppLocalDB.db.postDao().getPostByName(postId);
            MyApplication.mainHandler.post(()->{
                listener.onComplete(post);
            });
        });
    }
    public interface DeletePostListener{
        void onComplete();
    }
    public void DeletePost(Post post, DeletePostListener listener){
        MyApplication.executorService.execute(()->{
            AppLocalDB.db.postDao().delete(post);
            MyApplication.mainHandler.post(()->{
                listener.onComplete();
            });
        });
    }
}
