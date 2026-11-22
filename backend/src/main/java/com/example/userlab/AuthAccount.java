package com.example.userlab;
public record AuthAccount(Long id, String username, String passwordHash, Boolean enabled) {}
