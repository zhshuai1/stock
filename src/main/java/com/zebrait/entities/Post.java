package com.zebrait.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "POST")
public class Post {

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (!(obj instanceof Post))
			return false;
		Post p = (Post) obj;
		return this.getPostId().equals(p.getPostId()) && this.getTitle().equals(p.getTitle());
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "POST_ID")
	Integer postId;

	@Column(name = "TITLE")
	String title;

	@Column(name = "POST_DATE")
	Date postDate;

	public Integer getPostId() {
		return postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

}
