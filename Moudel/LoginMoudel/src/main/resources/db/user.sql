
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
                            `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户Id',
                            `name` varchar(10) DEFAULT NULL COMMENT '用户姓名',
                            `gender` char(4) DEFAULT NULL COMMENT '用户性别',
                            `email` varchar(20) DEFAULT NULL COMMENT '邮箱',
                            `phone` varchar(11) DEFAULT NULL COMMENT '电话号码',
                            `address` varchar(64) DEFAULT NULL COMMENT '联系地址',
                            `createTime` date DEFAULT NULL COMMENT '创建时间',
                            `state` int(8) DEFAULT 0 COMMENT '在线状态',
                            `expireTime` char(8) DEFAULT NULL COMMENT '过期时间',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1942 DEFAULT CHARSET=utf8;