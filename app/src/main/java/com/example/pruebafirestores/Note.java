package com.example.pruebafirestores;

import com.google.firebase.firestore.Exclude;

public class Note {
    private String documentID;
    private String title;
    private String description;

    public Note(){}

    public Note(String title, String description)
    {
        this.title=title;
        this.description=description;
    }

    @Exclude
    public String getdocumentID()
    {
        return documentID;
    }

    public void setdocumentID(String documentID)
    {
        this.documentID=documentID;
    }

    public String getTitle()
    {
        return title;
    }
    public String getDescription()
    {
        return description;
    }
    public void setTitle(String title)
    {
        this.title=title;
    }
    public void setDescription(String description)
    {
        this.description=description;
    }
}
