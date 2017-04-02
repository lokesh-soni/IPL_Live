package com.iplplay2win.app;

/**
 * Created by Anand on 02-04-2017.
 */

class TeamSelect {


    String teamname ;
    int teamid;

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public int getTeamid() {
        return teamid;
    }

    public void setTeamid(int teamid) {
        this.teamid = teamid;
    }

    public TeamSelect( int teamid, String teamname) {
        this.teamname = teamname;
        this.teamid = teamid;
    }
}
