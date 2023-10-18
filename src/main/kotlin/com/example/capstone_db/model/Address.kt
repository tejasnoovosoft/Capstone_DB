package com.example.capstone_db.model

import jakarta.persistence.*

@Entity
data class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val addressId: Long? = null,
    @Column(name = "street", length = 20)
    val street: String,
    @Column(name = "city", length = 20)
    val city: String,
    @Column(name = "pin_code", length = 6)
    val pinCode: Long,

    @OneToMany(mappedBy = "address", cascade = [CascadeType.ALL])
    val users: MutableList<User>? = null
)