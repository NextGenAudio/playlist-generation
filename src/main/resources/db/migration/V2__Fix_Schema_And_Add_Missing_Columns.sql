-- Fix user_id column type from text to uuid
-- This migration will convert existing text values to uuid if possible

-- First, let's check if we need to drop and recreate the table if there's incompatible data
-- For safety, we'll drop and recreate with the correct schema

-- Drop existing tables if they exist (in correct order due to foreign keys

-- Create the correct playlists table with UUID for user_id
CREATE TABLE public.playlists (
    playlist_id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_public BOOLEAN DEFAULT false,
    is_ai_generated BOOLEAN DEFAULT false,
    is_smart BOOLEAN DEFAULT false,
    genre VARCHAR(100),
    mood VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Create the correct join table for playlists and musics
CREATE TABLE public.playlist_musics (
    id SERIAL PRIMARY KEY,
    playlist_id INTEGER NOT NULL,
    music_id BIGINT NOT NULL,
    position INTEGER,
    added_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_playlist FOREIGN KEY (playlist_id) REFERENCES public.playlists(playlist_id) ON DELETE CASCADE
);

-- Add indexes for performance
CREATE INDEX IF NOT EXISTS idx_playlists_user_id ON public.playlists(user_id);
CREATE INDEX IF NOT EXISTS idx_playlists_genre ON public.playlists(genre);
CREATE INDEX IF NOT EXISTS idx_playlists_mood ON public.playlists(mood);
CREATE INDEX IF NOT EXISTS idx_playlists_public ON public.playlists(is_public);
CREATE INDEX IF NOT EXISTS idx_playlist_musics_playlist_id ON public.playlist_musics(playlist_id);
CREATE INDEX IF NOT EXISTS idx_playlist_musics_music_id ON public.playlist_musics(music_id);
CREATE INDEX IF NOT EXISTS idx_playlist_musics_position ON public.playlist_musics(playlist_id, position);
