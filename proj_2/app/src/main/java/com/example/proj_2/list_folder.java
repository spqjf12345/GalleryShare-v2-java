package com.example.proj_2;

public class list_folder {
    int folderImage;
    String folderName;
    String area;

    int getImage(){
        return this.folderImage;
    }
    String getTitle(){
        return this.folderName;
    }
    String getArea(){return this.area;}

    list_folder(int image, String title, String area){
        this.folderImage=image;
        this.folderName=title;
        this.area = area;
    }

}
