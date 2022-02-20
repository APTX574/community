USe community;
/* CREATE TABLE `user` (

`id` INT(11) AUTO_INCREMENT,

`username` varchar(50) DEFAULT NULL,

`password` varchar(50) DEFAULT NULL,

`salt` varchar(50) DEFAULT NULL,

`email` varchar(100) DEFAULT NULL,

`type` int(11) DEFAULT NULL COMMENT 'O-普通用戸; 1-超扱管理員; 2-版主;',

`status` int(11) DEFAULT NULL COMMENT 'O未激活; 1-已激活;',

`activation_code` varchar(100) DEFAULT NULL,

`header_url` varchar(200) DEFAULT NULL,

`create_time` timestamp NULL DEFAULT NULL,

PRIMARY KEY(`id`)

KEY `index.username` ( `username` (20)), 

KEY `index.email` ( `email` (20)) 

) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET utf8 */


/* CREATE TABLE discuss_post (

`id` int(11) NOT NULL AUTO_INCREMENT,

`user_id` int(11) DEFAULT NULL,

`title` varchar(100) DEFAULT NULL,

`content`text,

`type` int(11) DEFAULT NULL COMMENT "0-普通;1-置顶;",

`status` int(11) DEFAULT NULL COMMENT '0-正常;1-精华;2-拉黑;',
`create_time` timestamp NULL DEFAULT NULL,
`comment_count` int(11) DEFAULT NULL,
`score` double DEFAULT NULL,
PRIMARY KEY(`id`),
FOREIGN KEY(user_id) REFERENCES user(id)

) ENGINE=InnoDB AUTO_INCREMENT=281 DEFAULT CHARSET=utf8 */



CREATE TABLE comment (

`id` int(11) NOT NULL AUTO_INCREMENT,

`user_id` int(11) DEFAULT NULL,

`entity_type` int(11) DEFAULT NULL COMMENT "0-帖子;1-评论;",

`entity_id` int(11) DEFAULT NULL ,

`target_id` int(11) DEFAULT NULL  ,

`content` text,

`status` int(11) DEFAULT NULL COMMENT '0-正常;1-精华;2-拉黑;',

`create_time` timestamp NULL DEFAULT NULL,


PRIMARY KEY(`id`),
FOREIGN KEY(user_id) REFERENCES user(id)

) ENGINE=InnoDB AUTO_INCREMENT=281 DEFAULT CHARSET=utf8