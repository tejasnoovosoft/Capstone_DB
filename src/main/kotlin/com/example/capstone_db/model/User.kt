package com.example.capstone_db.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val userId: Long? = null,
    @Column(name = "username", unique = true)
    val username: String,
    @Column(name = "email" , unique = true)
    val email: String,
    @Column(name = "password")
    val password: String,
    @Column(name = "role")
    var role: String,
    @OneToMany(mappedBy = "user")
    val cart: MutableList<Order> = mutableListOf()
) {
    @PrePersist
    fun setYourProperty() {
        this.role = role.uppercase(Locale.getDefault())
    }
}