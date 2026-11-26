package com.example.blog;
import jakarta.persistence.*;
import java.util.*;
@Entity
public class BlogPost {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false) public String title;
 @Column(nullable=false,length=10000) public String content;
 @OneToMany(mappedBy="post",cascade=CascadeType.ALL,orphanRemoval=true) public List<Comment> comments=new ArrayList<>();
 public void addComment(Comment c){ c.post=this; comments.add(c); }
}
