package com.example.blog;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
@RestController
@RequestMapping("/api/posts")
@Transactional
public class BlogPostController {
 private final BlogPostRepository posts;
 public BlogPostController(BlogPostRepository posts){this.posts=posts;}
 record CommentInput(String author,String text){}
 record PostInput(String title,String content,List<CommentInput> comments){}
 private String required(String s,int max){if(s==null||s.isBlank()||s.length()>max)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid or missing field");return s;}
 private BlogPost find(Long id){return posts.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not found"));}
 private Comment comment(CommentInput in){if(in==null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST);Comment c=new Comment();c.author=required(in.author(),255);c.text=required(in.text(),2000);return c;}
 private void apply(BlogPost p,PostInput in){p.title=required(in.title(),255);p.content=required(in.content(),10000);if(in.comments()!=null){p.comments.clear();for(var c:in.comments())p.addComment(comment(c));}}
 @GetMapping public List<BlogPost> all(){return posts.findAll();}
 @GetMapping("/{id}") public BlogPost get(@PathVariable Long id){return find(id);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public BlogPost create(@RequestBody PostInput in){BlogPost p=new BlogPost();apply(p,in);return posts.saveAndFlush(p);}
 @PutMapping("/{id}") public BlogPost update(@PathVariable Long id,@RequestBody PostInput in){BlogPost p=find(id);apply(p,in);return posts.saveAndFlush(p);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){posts.delete(find(id));}
 @PostMapping("/{id}/comments") @ResponseStatus(HttpStatus.CREATED) public BlogPost add(@PathVariable Long id,@RequestBody CommentInput in){BlogPost p=find(id);p.addComment(comment(in));return posts.saveAndFlush(p);}
 @DeleteMapping("/{id}/comments/{cid}") @ResponseStatus(HttpStatus.NO_CONTENT) public void remove(@PathVariable Long id,@PathVariable Long cid){BlogPost p=find(id);if(!p.comments.removeIf(c->c.id.equals(cid)))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Comment not found");posts.saveAndFlush(p);}
}
