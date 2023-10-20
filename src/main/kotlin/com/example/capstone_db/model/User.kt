package com.example.capstone_db.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    val userId: Long? = null,
    @Column(name = "username")
    val username: String,
    @Column(name = "email" , unique = true)
    val email: String,

    @ManyToOne
    @JoinColumn(name = "address_id")
    val address: Address,

    @OneToMany(mappedBy = "user")
    val cart: MutableList<Order> = mutableListOf()
)