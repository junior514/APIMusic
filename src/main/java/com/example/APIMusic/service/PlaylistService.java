package com.example.APIMusic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.APIMusic.repository.PlaylistRepository;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    public Long contarTotalPlaylists() {
        return playlistRepository.count();
    }

    // Puedes agregar más métodos según lo requiera tu lógica de negocio
}
