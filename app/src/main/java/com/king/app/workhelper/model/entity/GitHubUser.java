package com.king.app.workhelper.model.entity;

/**
 * Created by VanceKing on 2017/1/6 0006.
 */

public class GitHubUser {

    /**
     * login : Guolei1130
     * id : 12966526
     * avatar_url : https://avatars2.githubusercontent.com/u/12966526?v=3
     * gravatar_id :
     * url : https://api.github.com/users/Guolei1130
     * html_url : https://github.com/Guolei1130
     * followers_url : https://api.github.com/users/Guolei1130/followers
     * following_url : https://api.github.com/users/Guolei1130/following{/other_user}
     * gists_url : https://api.github.com/users/Guolei1130/gists{/gist_id}
     * starred_url : https://api.github.com/users/Guolei1130/starred{/owner}{/repo}
     * subscriptions_url : https://api.github.com/users/Guolei1130/subscriptions
     * organizations_url : https://api.github.com/users/Guolei1130/orgs
     * repos_url : https://api.github.com/users/Guolei1130/repos
     * events_url : https://api.github.com/users/Guolei1130/events{/privacy}
     * received_events_url : https://api.github.com/users/Guolei1130/received_events
     * type : User
     * site_admin : false
     * name : _StriveG
     * company : null
     * blog : http://blog.csdn.net/qq_21430549
     * location : Beijing,China
     * email : 1120832563@qq.com
     * hireable : null
     * bio : 我好方啊
     * public_repos : 30
     * public_gists : 0
     * followers : 28
     * following : 82
     * created_at : 2015-06-19T12:10:51Z
     * updated_at : 2017-03-13T00:50:59Z
     */

    public String login;
    public int id;
    public String avatar_url;
    public String gravatar_id;
    public String url;
    public String html_url;
    public String followers_url;
    public String following_url;
    public String gists_url;
    public String starred_url;
    public String subscriptions_url;
    public String organizations_url;
    public String repos_url;
    public String events_url;
    public String received_events_url;
    public String type;
    public boolean site_admin;
    public String name;
    public Object company;
    public String blog;
    public String location;
    public String email;
    public Object hireable;
    public String bio;
    public int public_repos;
    public int public_gists;
    public int followers;
    public int following;
    public String created_at;
    public String updated_at;

    @Override public String toString() {
        return "GitHubUser{" +
                "login='" + login + '\'' +
                ", id=" + id +
                ", avatar_url='" + avatar_url + '\'' +
                ", gravatar_id='" + gravatar_id + '\'' +
                ", url='" + url + '\'' +
                ", html_url='" + html_url + '\'' +
                ", followers_url='" + followers_url + '\'' +
                ", following_url='" + following_url + '\'' +
                ", gists_url='" + gists_url + '\'' +
                ", starred_url='" + starred_url + '\'' +
                ", subscriptions_url='" + subscriptions_url + '\'' +
                ", organizations_url='" + organizations_url + '\'' +
                ", repos_url='" + repos_url + '\'' +
                ", events_url='" + events_url + '\'' +
                ", received_events_url='" + received_events_url + '\'' +
                ", type='" + type + '\'' +
                ", site_admin=" + site_admin +
                ", name='" + name + '\'' +
                ", company=" + company +
                ", blog='" + blog + '\'' +
                ", location='" + location + '\'' +
                ", email='" + email + '\'' +
                ", hireable=" + hireable +
                ", bio='" + bio + '\'' +
                ", public_repos=" + public_repos +
                ", public_gists=" + public_gists +
                ", followers=" + followers +
                ", following=" + following +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
