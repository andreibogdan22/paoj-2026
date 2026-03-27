package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {
    private String name;
    private Song[] songs;

    Playlist(String name) {
        this.name = name;
        this.songs = new Song[0];
    }

    public String getName() { return this.name; }

    public void addSong(Song song) {
        Song[] newSongs = new Song[this.songs.length + 1];
        System.arraycopy(this.songs, 0, newSongs, 0, this.songs.length);
        newSongs[newSongs.length - 1] = song;
        this.songs = newSongs;
    }

    public void printSortedByTitle() {
        Song[] copy = this.songs.clone();
        Arrays.sort(copy);
        for (Song s : copy) {
            System.out.println(s);
        }
    }

    public int getTotalDuration() {
        int total = 0;
        for (Song s: songs) {
            total += s.durationSeconds();
        }
        return total;
    }

    public void printSortedByDuration() {
        Song[] copy = this.songs.clone();
        Arrays.sort(copy, new SongDurationComparator());
        for (Song s : copy) {
            System.out.println(s);
        }
    }
}
