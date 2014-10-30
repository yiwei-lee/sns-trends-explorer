package edu.nyu.data;

import java.util.Date;

enum PostType {
  FB, TWITTER
}

public class Post {
  private PostType type;
  private User user;
  private Date time;
  private Location loc;
  private int replyCount;
  private int repostCount;
  private int likedCount;
  private String content;

  PostType getType() {
    return type;
  }

  void setType(PostType type) {
    this.type = type;
  }

  User getUser() {
    return user;
  }

  void setUser(User user) {
    this.user = user;
  }

  Date getTime() {
    return time;
  }

  void setTime(Date time) {
    this.time = time;
  }

  Location getLoc() {
    return loc;
  }

  void setLoc(Location loc) {
    this.loc = loc;
  }

  int getReplyCount() {
    return replyCount;
  }

  void setReplyCount(int replyCount) {
    this.replyCount = replyCount;
  }

  int getRepostCount() {
    return repostCount;
  }

  void setRepostCount(int repostCount) {
    this.repostCount = repostCount;
  }

  int getLikedCount() {
    return likedCount;
  }

  void setLikedCount(int likedCount) {
    this.likedCount = likedCount;
  }

  String getContent() {
    return content;
  }

  void setContent(String content) {
    this.content = content;
  }
}
