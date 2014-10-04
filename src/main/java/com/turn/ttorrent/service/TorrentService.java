package com.turn.ttorrent.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("service")
public class TorrentService {

	@GET
	@Path("/alltorrents")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllTorrents(){
		return "{\"torrent\":\"torrents\"}";
	}
	
	@GET
	@Path("/torrent/{id}")
	public String getTorrent(@PathParam("id") String torrentName){
		return "";
	}
}
