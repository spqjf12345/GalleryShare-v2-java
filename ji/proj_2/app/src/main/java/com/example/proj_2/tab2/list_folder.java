package com.example.proj_2.tab2;

public class list_folder {
    public int folderImage;
    public String folderName;
    public String area;

    int getImage(){
        return this.folderImage;
    }
    String getTitle(){ return this.folderName; }
    String getArea(){ return this.area; }

    public list_folder(int image, String folderName, String area){
        this.folderImage = image;
        this.folderName = folderName;
        this.area = area;
    }

}
