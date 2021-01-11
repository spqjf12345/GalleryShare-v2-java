package com.example.proj_2;

public class list_folder {
    int folder;
    String folderName;

    int getImage(){
        return this.folder;
    }
    String getTitle(){
        return this.folderName;
    }

    list_folder(int image, String title){
        this.folder=image;
        this.folderName=title;
    }

}
