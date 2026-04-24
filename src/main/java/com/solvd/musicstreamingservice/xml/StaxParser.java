package com.solvd.musicstreamingservice.xml;

import com.solvd.musicstreamingservice.xml.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * StAX (Streaming API for XML) parser implementation.
 * StAX is a pull parser — the application pulls events from the parser on demand.
 *
 * <p>XPath examples for this XML structure:</p>
 * <ul>
 *   <li>{@code /musicService/serviceName} — select the service name</li>
 *   <li>{@code /musicService/artists/artist/name} — select all artist names</li>
 *   <li>{@code /musicService/artists/artist/albums/album/songs/song[@id='1']/title} — select song title with id=1</li>
 *   <li>{@code /musicService/users/user[@id='1']/username} — select username of user with id=1</li>
 *   <li>{@code //song[duration > 200]/title} — select titles of songs longer than 200 seconds</li>
 * </ul>
 */
public class StaxParser implements Parser {

    private static final Logger LOGGER = LogManager.getLogger(StaxParser.class);

    @Override
    public XmlMusicService parse(String filePath) {
        XmlMusicService service = new XmlMusicService();
        List<XmlArtist> artists = new ArrayList<>();
        List<XmlUser> users = new ArrayList<>();

        XmlArtist currentArtist = null;
        XmlAlbum currentAlbum = null;
        XmlSong currentSong = null;
        XmlUser currentUser = null;

        // Track nesting: which parent element are we inside?
        String currentElement = "";
        boolean inArtists = false;
        boolean inAlbums = false;
        boolean inSongs = false;
        boolean inUsers = false;

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filePath));

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        currentElement = reader.getLocalName();

                        switch (currentElement) {
                            case "artists" -> inArtists = true;
                            case "albums" -> inAlbums = true;
                            case "songs" -> inSongs = true;
                            case "users" -> inUsers = true;
                            case "artist" -> {
                                if (inArtists) currentArtist = new XmlArtist();
                            }
                            case "album" -> {
                                if (inAlbums) currentAlbum = new XmlAlbum();
                            }
                            case "song" -> {
                                if (inSongs) {
                                    currentSong = new XmlSong();
                                    String idAttr = reader.getAttributeValue(null, "id");
                                    if (idAttr != null) currentSong.setId(Integer.parseInt(idAttr));
                                }
                            }
                            case "user" -> {
                                if (inUsers) {
                                    currentUser = new XmlUser();
                                    String idAttr = reader.getAttributeValue(null, "id");
                                    if (idAttr != null) currentUser.setId(Integer.parseInt(idAttr));
                                }
                            }
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        String text = reader.getText().trim();
                        if (text.isEmpty()) break;

                        // Song fields
                        if (inSongs && currentSong != null) {
                            switch (currentElement) {
                                case "title" -> currentSong.setTitle(text);
                                case "artistName" -> currentSong.setArtistName(text);
                                case "genreName" -> currentSong.setGenreName(text);
                                case "duration" -> currentSong.setDuration(Integer.parseInt(text));
                                case "availableOffline" -> currentSong.setAvailableOffline(Boolean.parseBoolean(text));
                                case "addedAt" -> currentSong.setAddedAt(LocalDateTime.parse(text));
                            }
                        }
                        // Album fields (not inside songs)
                        else if (inAlbums && currentAlbum != null && !inSongs) {
                            switch (currentElement) {
                                case "title" -> currentAlbum.setTitle(text);
                                case "releaseDate" -> currentAlbum.setReleaseDate(text);
                                case "totalTracks" -> currentAlbum.setTotalTracks(Integer.parseInt(text));
                                case "explicit" -> currentAlbum.setExplicit(Boolean.parseBoolean(text));
                                case "createdAt" -> currentAlbum.setCreatedAt(LocalDateTime.parse(text));
                            }
                        }
                        // Artist fields (not inside albums)
                        else if (inArtists && currentArtist != null && !inAlbums) {
                            switch (currentElement) {
                                case "name" -> currentArtist.setName(text);
                                case "country" -> currentArtist.setCountry(text);
                                case "debutYear" -> currentArtist.setDebutYear(Integer.parseInt(text));
                                case "verified" -> currentArtist.setVerified(Boolean.parseBoolean(text));
                                case "lastUpdated" -> currentArtist.setLastUpdated(LocalDateTime.parse(text));
                            }
                        }
                        // User fields
                        else if (inUsers && currentUser != null) {
                            switch (currentElement) {
                                case "username" -> currentUser.setUsername(text);
                                case "email" -> currentUser.setEmail(text);
                                case "subscriptionType" -> currentUser.setSubscriptionType(text);
                                case "premium" -> currentUser.setPremium(Boolean.parseBoolean(text));
                                case "downloadLimit" -> currentUser.setDownloadLimit(Integer.parseInt(text));
                                case "registeredAt" -> currentUser.setRegisteredAt(LocalDateTime.parse(text));
                            }
                        }
                        // Root level
                        else {
                            switch (currentElement) {
                                case "serviceName" -> service.setServiceName(text);
                                case "totalStreams" -> service.setTotalStreams(Integer.parseInt(text));
                            }
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        String endElement = reader.getLocalName();
                        switch (endElement) {
                            case "song" -> {
                                if (currentAlbum != null && currentSong != null) {
                                    currentAlbum.getSongs().add(currentSong);
                                    currentSong = null;
                                }
                            }
                            case "album" -> {
                                if (currentArtist != null && currentAlbum != null) {
                                    currentArtist.getAlbums().add(currentAlbum);
                                    currentAlbum = null;
                                }
                            }
                            case "artist" -> {
                                if (currentArtist != null) {
                                    artists.add(currentArtist);
                                    currentArtist = null;
                                }
                            }
                            case "user" -> {
                                if (currentUser != null) {
                                    users.add(currentUser);
                                    currentUser = null;
                                }
                            }
                            case "artists" -> inArtists = false;
                            case "albums" -> inAlbums = false;
                            case "songs" -> inSongs = false;
                            case "users" -> inUsers = false;
                        }
                        break;
                }
            }

            reader.close();
            service.setArtists(artists);
            service.setUsers(users);
            LOGGER.info("StAX parsing completed: {}", service);

        } catch (Exception e) {
            LOGGER.error("StAX parsing error: {}", e.getMessage());
        }

        return service;
    }
}
