package com.record.myplace.placeLike.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
	name = "place_like",
	uniqueConstraints = @UniqueConstraint(name = "uk_place_like_user_place", columnNames = {"useremail", "place_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class PlaceLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "useremail", nullable = false, length = 255)
	private String useremail;
	
	@Column(name = "place_id", nullable = false, length = 64)
	private String placeId;

	@Column(name = "place_name", length = 255)
	private String placeName;
	
	@Column(name = "address", length = 255)
	private String address;
	
	@Column(name = "category", length = 255)
	private String category;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@PrePersist
	void prePersist() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
	}
}
