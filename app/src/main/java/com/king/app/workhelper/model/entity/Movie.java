package com.king.app.workhelper.model.entity;

import java.util.List;

/**
 * Created by VanceKing on 2016/12/6 0006.
 */

public class Movie {

    /**
     * rating : {"max":10,"average":9.5,"stars":"50","min":0}
     * genres : ["剧情","喜剧","爱情"]
     * title : 美丽人生
     * casts : [{"alt":"https://movie.douban.com/celebrity/1041004/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/26764.jpg","large":"https://img3.doubanio.com/img/celebrity/large/26764.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/26764.jpg"},"name":"罗伯托·贝尼尼","id":"1041004"},{"alt":"https://movie.douban.com/celebrity/1000375/","avatars":{"small":"https://img1.doubanio.com/img/celebrity/small/9548.jpg","large":"https://img1.doubanio.com/img/celebrity/large/9548.jpg","medium":"https://img1.doubanio.com/img/celebrity/medium/9548.jpg"},"name":"尼可莱塔·布拉斯基","id":"1000375"},{"alt":"https://movie.douban.com/celebrity/1000368/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/45590.jpg","large":"https://img3.doubanio.com/img/celebrity/large/45590.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/45590.jpg"},"name":"乔治·坎塔里尼","id":"1000368"}]
     * collect_count : 478293
     * original_title : La vita è bella
     * subtype : movie
     * directors : [{"alt":"https://movie.douban.com/celebrity/1041004/","avatars":{"small":"https://img3.doubanio.com/img/celebrity/small/26764.jpg","large":"https://img3.doubanio.com/img/celebrity/large/26764.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/26764.jpg"},"name":"罗伯托·贝尼尼","id":"1041004"}]
     * year : 1997
     * images : {"small":"https://img3.doubanio.com/view/movie_poster_cover/ipst/public/p510861873.jpg","large":"https://img3.doubanio.com/view/movie_poster_cover/lpst/public/p510861873.jpg","medium":"https://img3.doubanio.com/view/movie_poster_cover/spst/public/p510861873.jpg"}
     * alt : https://movie.douban.com/subject/1292063/
     * id : 1292063
     */

    public RatingBean rating;
    public String title;
    public int collect_count;
    public String original_title;
    public String subtype;
    public String year;
    public ImagesBean images;
    public String alt;
    public String id;
    public List<String> genres;
    public List<CastsBean> casts;
    public List<DirectorsBean> directors;

    public static class RatingBean {
        /**
         * max : 10
         * average : 9.5
         * stars : 50
         * min : 0
         */

        public int max;
        public double average;
        public String stars;
        public int min;
    }

    public static class ImagesBean {
        /**
         * small : https://img3.doubanio.com/view/movie_poster_cover/ipst/public/p510861873.jpg
         * large : https://img3.doubanio.com/view/movie_poster_cover/lpst/public/p510861873.jpg
         * medium : https://img3.doubanio.com/view/movie_poster_cover/spst/public/p510861873.jpg
         */

        public String small;
        public String large;
        public String medium;
    }

    public static class CastsBean {
        /**
         * alt : https://movie.douban.com/celebrity/1041004/
         * avatars : {"small":"https://img3.doubanio.com/img/celebrity/small/26764.jpg","large":"https://img3.doubanio.com/img/celebrity/large/26764.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/26764.jpg"}
         * name : 罗伯托·贝尼尼
         * id : 1041004
         */

        public String alt;
        public AvatarsBean avatars;
        public String name;
        public String id;

        public static class AvatarsBean {
            /**
             * small : https://img3.doubanio.com/img/celebrity/small/26764.jpg
             * large : https://img3.doubanio.com/img/celebrity/large/26764.jpg
             * medium : https://img3.doubanio.com/img/celebrity/medium/26764.jpg
             */

            public String small;
            public String large;
            public String medium;
        }
    }

    public static class DirectorsBean {
        /**
         * alt : https://movie.douban.com/celebrity/1041004/
         * avatars : {"small":"https://img3.doubanio.com/img/celebrity/small/26764.jpg","large":"https://img3.doubanio.com/img/celebrity/large/26764.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/26764.jpg"}
         * name : 罗伯托·贝尼尼
         * id : 1041004
         */

        public String alt;
        public AvatarsBeanX avatars;
        public String name;
        public String id;

        public static class AvatarsBeanX {
            /**
             * small : https://img3.doubanio.com/img/celebrity/small/26764.jpg
             * large : https://img3.doubanio.com/img/celebrity/large/26764.jpg
             * medium : https://img3.doubanio.com/img/celebrity/medium/26764.jpg
             */

            public String small;
            public String large;
            public String medium;
        }
    }

    @Override
    public String toString() {
        return "Movie{" +
                "rating=" + rating +
                ", title='" + title + '\'' +
                ", collect_count=" + collect_count +
                ", original_title='" + original_title + '\'' +
                ", subtype='" + subtype + '\'' +
                ", year='" + year + '\'' +
                ", images=" + images +
                ", alt='" + alt + '\'' +
                ", id='" + id + '\'' +
                ", genres=" + genres +
                ", casts=" + casts +
                ", directors=" + directors +
                '}';
    }
}
