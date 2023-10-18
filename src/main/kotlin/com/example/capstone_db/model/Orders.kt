package com.example.capstone_db.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "orders")
data class Orders(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val orderId: Long? = null,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    val products: MutableList<OrderItem>? = null,

    @Column(name = "order_date")
    val orderDate: LocalDate? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User
)
