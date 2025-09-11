-- Create the simplified playlists table with UUID for user_id
CREATE TABLE public.playlists (
                                  playlist_id SERIAL PRIMARY KEY,
                                  user_id UUID NOT NULL,
                                  name VARCHAR(255) NOT NULL,
                                  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Create the simplified join table for playlists and musics
CREATE TABLE public.playlist_musics (
                                        id SERIAL PRIMARY KEY,
                                        playlist_id INTEGER NOT NULL,
                                        music_id BIGINT NOT NULL,
                                        position INTEGER,
                                        added_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                                        CONSTRAINT fk_playlist FOREIGN KEY (playlist_id) REFERENCES public.playlists(playlist_id) ON DELETE CASCADE,
                                        CONSTRAINT fk_music FOREIGN KEY (music_id) REFERENCES public.musics(id) ON DELETE CASCADE
);

-- Add indexes for performance
CREATE INDEX IF NOT EXISTS idx_playlists_user_id ON public.playlists(user_id);
CREATE INDEX IF NOT EXISTS idx_playlist_musics_playlist_id ON public.playlist_musics(playlist_id);
CREATE INDEX IF NOT EXISTS idx_playlist_musics_music_id ON public.playlist_musics(music_id);