package com.example.capstone_db.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val orderId: Long? = null,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.REMOVE])
    val products: MutableList<OrderItem>? = null,

    @Column(name = "order_date")
    var orderDate: LocalDate? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null
) {
    @PrePersist
    fun prePersist() {
        orderDate = LocalDate.now()
    }
}
