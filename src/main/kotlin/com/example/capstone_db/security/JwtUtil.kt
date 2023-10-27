package com.example.capstone_db.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function

@Component
class JwtUtil(@Value("\${jwt.secret}") private val secret: String) {

    @Value("\${jwt.expiration.ms}")
    private val expiration: Long = 86400000

    fun generateToken(userDetails: UserDetails): String {
        return createToken(userDetails.username)
    }

    private fun createToken(subject: String): String {
        val expirationDate = Date(System.currentTimeMillis() + expiration)
        return Jwts.builder().setSubject(subject).setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, secret).compact()             // string representation
    }

    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    private fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
    }

    fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun extractClaims(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    /*    fun setTokenCookie(response: HttpServletResponse, token: String) {
            val cookie = Cookie("jwt", token)
            cookie.path = "/auth/current"
            cookie.isHttpOnly = true // Frontend cannot access the cookie
            cookie.secure = true
            response.addCookie(cookie)
        }*/
}
