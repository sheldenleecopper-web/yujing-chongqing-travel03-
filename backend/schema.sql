-- 渝境智旅 数据库初始化脚本
-- 执行：mysql -u root -p yujing < schema.sql
SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS yujing DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE yujing;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id`          BIGINT PRIMARY KEY AUTO_INCREMENT,
  `username`    VARCHAR(64)  NOT NULL UNIQUE COMMENT '用户名',
  `password`    VARCHAR(128) NOT NULL COMMENT 'BCrypt 密文',
  `role`        VARCHAR(16)  NOT NULL DEFAULT 'USER' COMMENT 'USER / ADMIN',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 去处表（景点 / 美食 / 活动）
CREATE TABLE IF NOT EXISTS `spot` (
  `id`         BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name`       VARCHAR(128)   NOT NULL,
  `type`       VARCHAR(32)    COMMENT 'attraction / food / activity',
  `region`     VARCHAR(64)    COMMENT '所属区县',
  `tags`       VARCHAR(255)  COMMENT '逗号分隔标签',
  `cover`      VARCHAR(512)   COMMENT '封面图 URL',
  `intro`      TEXT,
  `heat_index` INT            DEFAULT 0 COMMENT '热度',
  `avg_cost`   DECIMAL(10,2)  COMMENT '人均消费',
  `open_time`  VARCHAR(128),
  `address`    VARCHAR(255),
  `lng`        DECIMAL(10,6),
  `lat`        DECIMAL(10,6),
  `description`   TEXT         COMMENT '详细介绍',
  `price_type`    VARCHAR(16)  NOT NULL DEFAULT 'unknown' COMMENT 'free/paid/unknown',
  `is_classic`    TINYINT      NOT NULL DEFAULT 0 COMMENT '经典必打卡',
  `scene`         VARCHAR(16)  COMMENT 'nature/history',
  `activity_flag` TINYINT      NOT NULL DEFAULT 0 COMMENT '是否关联热门活动',
  `play_tip`      VARCHAR(255) COMMENT '游玩建议',
  UNIQUE KEY `uk_spot_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 行程表（每个用户默认一条行程）
CREATE TABLE IF NOT EXISTS `trip` (
  `id`          BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id`     BIGINT      NOT NULL,
  `title`       VARCHAR(64) NOT NULL DEFAULT '我的重庆行程',
  `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `idx_trip_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `trip_spot` (
  `id`          BIGINT PRIMARY KEY AUTO_INCREMENT,
  `trip_id`     BIGINT   NOT NULL,
  `spot_id`     BIGINT   NOT NULL,
  `sort_no`     INT      NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `uk_trip_spot` (`trip_id`, `spot_id`),
  KEY `idx_trip_spot_trip` (`trip_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 非遗表
CREATE TABLE IF NOT EXISTS `heritage` (
  `id`              BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name`            VARCHAR(128) NOT NULL,
  `level`           VARCHAR(32)  COMMENT '国家级 / 市级',
  `category`        VARCHAR(64),
  `cover`           VARCHAR(512),
  `intro`           TEXT,
  `inheritor`       VARCHAR(128) COMMENT '传承人',
  `experience_spot` VARCHAR(255) COMMENT '体验地点',
  `related_spot_id` BIGINT       COMMENT '关联去处 ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 酒店表
CREATE TABLE IF NOT EXISTS `hotel` (
  `id`          BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name`        VARCHAR(128) NOT NULL,
  `region`      VARCHAR(64),
  `price_range` VARCHAR(32)  COMMENT '经济 / 适中 / 豪华',
  `intro`       TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 攻略表
CREATE TABLE IF NOT EXISTS `guide` (
  `id`               BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id`          BIGINT,
  `title`            VARCHAR(255),
  `params_summary`   VARCHAR(512) COMMENT '生成参数摘要',
  `spot_ids`         VARCHAR(512) COMMENT '关联去处 ID，逗号分隔',
  `markdown_content` LONGTEXT,
  `poster_url`       VARCHAR(512),
  `is_saved`         TINYINT      DEFAULT 0 COMMENT '0 未保存 1 已保存',
  `create_time`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `idx_guide_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 收藏表
CREATE TABLE IF NOT EXISTS `favorite` (
  `id`          BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id`    BIGINT  NOT NULL,
  `spot_id`    BIGINT  NOT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `uk_user_spot` (`user_id`, `spot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 浏览历史表
CREATE TABLE IF NOT EXISTS `browse_history` (
  `id`          BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id`    BIGINT  NOT NULL,
  `spot_id`    BIGINT  NOT NULL,
  `browse_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `idx_history_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始管理员账号：admin / admin123（BCrypt 密文）
INSERT INTO `user` (`username`, `password`, `role`) VALUES
('admin', '$2a$10$sQ6Hm2igH7aVabyLm8NrFOQtJS60Wc3los261xokVdqJN7Mm5nHve', 'ADMIN')
ON DUPLICATE KEY UPDATE `password` = VALUES(`password`), `role` = 'ADMIN';

-- ==================== 示例数据 ====================

INSERT INTO `spot` (`name`, `type`, `region`, `tags`, `cover`, `intro`, `heat_index`, `avg_cost`, `open_time`, `address`, `lng`, `lat`) VALUES
('洪崖洞', 'attraction', '渝中区', '夜景,吊脚楼,打卡', '', '依山而建的巴渝传统吊脚楼群，夜晚灯火通明，是重庆最具代表性的地标之一。', 98, 0.00, '全天开放', '重庆市渝中区嘉陵江滨江路88号', 106.583, 29.564),
('解放碑', 'attraction', '渝中区', '地标,商圈,历史', '', '抗战胜利纪功碑，重庆城市原点，周边是全国著名商圈。', 95, 0.00, '全天开放', '重庆市渝中区民权路', 106.576, 29.560),
('磁器口古镇', 'attraction', '沙坪坝区', '古镇,小吃,老街', '', '嘉陵江畔的千年古镇，青石板路与老茶馆，是体验老重庆市井生活的去处。', 92, 30.00, '全天开放', '重庆市沙坪坝区磁器口南街', 106.449, 29.582),
('长江索道', 'attraction', '渝中区', '交通,观景,老重庆', '', '横跨长江的空中客运索道，被称为"空中公交车"，可俯瞰两江交汇。', 90, 30.00, '07:30-22:30', '重庆市渝中区新华路151号', 106.588, 29.562),
('武隆天生三桥', 'attraction', '武隆区', '自然,喀斯特,世界遗产', '', '世界自然遗产，三座天生石桥气势磅礴，是《变形金刚4》取景地。', 88, 135.00, '08:30-17:00', '重庆市武隆区仙女山镇', 107.933, 29.432),
('大足石刻', 'attraction', '大足区', '世界遗产,石刻,佛教', '', '世界文化遗产，唐宋时期摩崖石刻造像群，造像精美绝伦。', 86, 115.00, '08:30-18:00', '重庆市大足区龙岗镇', 105.706, 29.703),
('重庆火锅', 'food', '渝中区', '美食,麻辣,必吃', '', '以牛油锅底、麻辣鲜香著称，重庆的城市名片。推荐老灶火锅与九宫格。', 99, 90.00, '10:00-次日02:00', '重庆市渝中区各火锅店', 106.576, 29.560),
('重庆小面', 'food', '渝中区', '美食,早餐,平价', '', '麻辣鲜香的重庆特色早餐面食，一碗小面开启山城的一天。', 94, 12.00, '06:30-14:00', '重庆市渝中区大街小巷', 106.576, 29.560),
('酸辣粉', 'food', '渝中区', '美食,小吃,街边', '', '红油酸辣粉，粉条劲道，配料丰富，是解放碑一带的经典小吃。', 85, 10.00, '09:00-22:00', '重庆市渝中区八一路好吃街', 106.576, 29.560),
('两江游轮夜游', 'activity', '渝中区', '夜景,游船,两江', '', '夜游长江与嘉陵江，观赏两岸灯火与跨江大桥，约60分钟航程。', 91, 128.00, '19:00-22:00', '重庆市渝中区朝天门码头', 106.589, 29.571),
('川剧变脸体验', 'activity', '渝中区', '非遗,演出,体验', '', '在茶馆观看川剧变脸、吐火等绝技，感受巴蜀传统戏曲魅力。', 87, 80.00, '19:30-21:30', '重庆市渝中区洪崖洞巴渝剧院', 106.583, 29.564),
('南山一棵树观景', 'activity', '南岸区', '夜景,观景台,摄影', '', '俯瞰渝中半岛夜景的最佳位置，可拍到渝中半岛全景。', 89, 30.00, '09:00-22:30', '重庆市南岸区南山植物园旁', 106.601, 29.549);

-- 景点榜完整数据（同名景点按 name 覆盖，保证可重复执行）
INSERT INTO `spot`
(`name`,`type`,`region`,`tags`,`cover`,`intro`,`description`,`heat_index`,`avg_cost`,
 `price_type`,`is_classic`,`scene`,`activity_flag`,`play_tip`,`open_time`,`address`)
VALUES
('洪崖洞','attraction','渝中区','夜景,吊脚楼,打卡','/images/spots/hongyadong.jpg',
 '山城夜景的代表，现实版的千与千寻。',
 '洪崖洞依山就势，沿嘉陵江而建，是重庆最具代表性的吊脚楼建筑群。夜幕降临，吊脚楼灯火层叠，与江面倒影交相辉映，构成山城最经典的夜景画面。',
 98,0.00,'free',1,'history',1,
 '建议傍晚先到千厮门大桥取景，再进入景区自 11 层往下逛。','全天开放（亮灯约 19:00-23:00）','重庆市渝中区嘉陵江滨江路 88 号'),

('长江索道','attraction','渝中区','观景,索道,老重庆','/images/spots/changjiang-suodao.jpg',
 '横跨两江，俯瞰山城的浪漫。',
 '长江索道连接渝中区新华路与南岸区上新街，全长约 1166 米，被称为老重庆的“空中公交车”。轿厢横渡长江时，可俯瞰两岸楼宇与江面船只。',
 96,30.00,'paid',1,'history',1,
 '单程约 4 分钟，建议错峰乘坐并提前在官方渠道购票。','07:30-22:30','重庆市渝中区新华路 151 号'),

('磁器口古镇','attraction','沙坪坝区','古镇,老街,小吃','/images/spots/ciqikou.jpg',
 '千年古镇，感受老重庆的烟火气。',
 '磁器口始建于宋代，嘉陵江畔的青石板老街保存着巴渝民居与码头文化，茶馆、麻花铺与手工作坊遍布街巷，是体验老重庆市井生活的去处。',
 92,0.00,'free',1,'history',0,
 '从正街进入后拐入小巷，能避开人流看到更安静的旧宅院。','全天开放','重庆市沙坪坝区磁器口南街'),

('解放碑','attraction','渝中区','地标,商圈,历史','/images/spots/jiefangbei.jpg',
 '重庆的城市心脏，繁华与历史交融。',
 '解放碑即抗战胜利纪功碑暨人民解放纪念碑，是重庆的城市原点。碑体周围高楼林立、商圈繁华，步行范围内还能找到老巷与防空洞遗迹。',
 91,0.00,'free',1,'history',0,
 '夜晚灯光亮起后更适合拍照，周边步行可达八一路好吃街。','全天开放','重庆市渝中区民权路'),

('武隆天生三桥','attraction','武隆区','自然,喀斯特,世界遗产','/images/spots/wulong.jpg',
 '世界自然遗产，震撼的喀斯特奇观。',
 '天生三桥由天龙桥、青龙桥、黑龙桥三座天生石拱桥组成，桥下天坑幽深、植被葱郁，是武隆喀斯特世界自然遗产的核心景区。',
 88,135.00,'paid',1,'nature',0,
 '景区内以步行为主，建议穿防滑鞋并预留半天时间。','08:30-17:00','重庆市武隆区仙女山镇'),

('大足石刻','attraction','大足区','世界遗产,石刻,佛教','/images/spots/dazu.jpg',
 '千年石刻艺术，见证历史的厚重。',
 '大足石刻是唐宋时期摩崖造像的总称，以宝顶山、北山摩崖造像最为著名，题材涵盖佛教、道教与儒家，1999 年列入世界文化遗产名录。',
 86,115.00,'paid',1,'history',0,
 '宝顶山与北山相距较远，可安排一整天，建议请讲解。','08:30-18:00','重庆市大足区宝顶镇'),

('朝天门广场','attraction','渝中区','两江,地标,江景','/images/spots/chaotianmen.jpg',
 '两江交汇处，重庆的城市名片。',
 '朝天门位于长江与嘉陵江交汇处，是重庆古城门之一。广场可俯瞰两江汇流的“鸳鸯锅”水色，对岸是江北嘴与南滨路的城市天际线。',
 84,0.00,'free',1,'history',0,
 '傍晚到广场看江景，就近换乘两江游轮夜游。','全天开放','重庆市渝中区朝天门广场'),

('南山一棵树观景台','attraction','南岸区','夜景,观景台,摄影','/images/spots/nanshan.jpg',
 '俯瞰重庆全景，日落时分绝美。',
 '南山一棵树观景台位于南岸区南山，是观赏渝中半岛天际线的经典位置。日落到入夜，灯光沿江次第亮起，整座山城尽收眼底。',
 82,30.00,'paid',1,'nature',1,
 '日落后半小时灯光渐次亮起，是最佳拍摄时段，建议带三脚架。','09:00-22:30','重庆市南岸区南山植物园旁'),

('白公馆','attraction','沙坪坝区','红岩,历史,旧址','/images/spots/baigongguan.jpg',
 '歌乐山下的革命旧址，见证红岩历史。',
 '白公馆原为四川军阀白驹的郊外别墅，抗战至解放战争时期曾被用作监狱，现为红岩革命历史博物馆的重要组成部分。',
 80,0.00,'free',0,'history',0,
 '与渣滓洞相距不远，可沿歌乐山景区一并参观。','09:00-17:00（周一闭馆）','重庆市沙坪坝区壮志路'),

('渣滓洞','attraction','沙坪坝区','红岩,历史,旧址','/images/spots/zhazidong.jpg',
 '红岩精神的见证地，历史沉静而厚重。',
 '渣滓洞原为人工开采的小煤窑，后成为关押革命志士的监狱。旧址依山而建，保留着牢房、审讯室与部分史料陈列。',
 78,0.00,'free',0,'history',0,
 '参观以步行与展陈为主，建议避开节假日高峰。','09:00-17:00（周一闭馆）','重庆市沙坪坝区歌乐山'),

('歌乐山森林公园','attraction','沙坪坝区','登山,森林,徒步','/images/spots/geleshan.jpg',
 '城中森林，登山步道串联山城绿意。',
 '歌乐山位于沙坪坝区，森林覆盖率高，登山步道可俯瞰城区。山中散布红岩遗迹与老寺庙，是重庆市民周末徒步的常去之处。',
 76,0.00,'free',0,'nature',0,
 '穿舒适运动鞋，自带饮水，山路以石阶为主。','全天开放','重庆市沙坪坝区歌乐山街道'),

('鹅岭公园','attraction','渝中区','园林,瞰胜楼,城景','/images/spots/eling.jpg',
 '两江之上的老公园，瞰胜楼看城景。',
 '鹅岭公园建于清末园林基础之上，园内瞰胜楼可远眺两江与渝中半岛。相比热门观景台，这里人少安静，适合慢慢看城。',
 74,0.00,'free',0,'nature',0,
 '从鹅岭二厂步行过来，下午到瞰胜楼等日落。','06:00-22:00','重庆市渝中区鹅岭正街 176 号'),

('李子坝轻轨穿楼','attraction','渝中区','轻轨,城市奇观,打卡','/images/spots/liziba.jpg',
 '轻轨从楼中穿过，山城独有的城市奇观。',
 '轨道交通 2 号线李子坝站建在居民楼中，列车穿楼而过的场景成为重庆的城市符号。站旁设有观景平台，正对穿楼位置。',
 73,0.00,'free',0,'history',1,
 '在李子坝观景平台等候列车进站，班次间隔约 3-5 分钟。','观景平台全天开放','重庆市渝中区李子坝正街'),

('重庆中国三峡博物馆','attraction','渝中区','博物馆,三峡,巴渝文化','/images/spots/sanxia-bowuguan.jpg',
 '读懂巴渝与三峡的一站式博物馆。',
 '三峡博物馆位于人民广场，馆藏涵盖巴渝文化、三峡文物与抗战文物，常设展览系统呈现重庆从巴国到近代的历史脉络。',
 71,0.00,'free',1,'history',0,
 '与大礼堂、人民广场同一区域，可安排半天参观。','09:00-17:00（周一闭馆）','重庆市渝中区人民路 236 号'),

('罗汉寺','attraction','渝中区','古刹,佛教,闹市','/images/spots/luohan.jpg',
 '闹市中的千年古刹，香火与高楼相望。',
 '罗汉寺始建于北宋治平年间，是重庆主城著名的佛教寺院。寺内五百阿罗汉塑像与周边的现代高楼形成独特对比。',
 69,10.00,'paid',0,'history',0,
 '寺院与解放碑步行可达，参观时注意着装得体。','08:00-17:30','重庆市渝中区罗汉寺街 7 号'),

('十八梯传统风貌区','attraction','渝中区','老街,市井,夜景','/images/spots/shibati.jpg',
 '爬坡上坎的老街，找回老重庆的日常。',
 '十八梯连接上半城与下半城，是重庆传统街区。改造后保留石阶、老屋与市井业态，夜晚灯笼亮起，梯坎两侧烟火气十足。',
 68,0.00,'free',0,'history',0,
 '傍晚从较场口往下走，边逛边吃，最后到江边看夜景。','全天开放','重庆市渝中区十八梯'),

('湖广会馆','attraction','渝中区','会馆,古建,移民史','/images/spots/huguang.jpg',
 '清代移民会馆建筑群，木雕戏楼精美。',
 '湖广会馆建于清代，是“湖广填四川”移民历史的见证。现存禹王宫、齐安公所等建筑，戏楼木雕与院落格局保存完好。',
 66,30.00,'paid',0,'history',0,
 '与东水门城门、长江索道可串成一条步行线路。','09:00-17:00','重庆市渝中区芭蕉园 1 号'),

('巫山小三峡','attraction','巫山县','峡谷,游船,山水','/images/spots/wushan.jpg',
 '峡谷碧水与悬棺，长江支流的山水长卷。',
 '小三峡位于巫山县大宁河，由龙门峡、巴雾峡、滴翠峡组成。峡谷幽深、水色清碧，乘船途中还能看到古栈道孔与悬棺遗迹。',
 64,120.00,'paid',0,'nature',0,
 '需先到巫山县城码头乘船，全程约 4 小时。','08:00-16:00','重庆市巫山县大宁河'),

('酉阳桃花源','attraction','酉阳县','溶洞,田园,世外桃源','/images/spots/taohuayuan.jpg',
 '酉阳山水间的世外桃源，溶洞与田园相连。',
 '酉阳桃花源景区由古桃源、伏羲洞与酉州古城组成。洞内钟乳石千姿百态，洞外田园村落安静，呼应陶渊明笔下的桃花源意象。',
 62,100.00,'paid',0,'nature',0,
 '洞内湿滑，建议穿防滑鞋，预留半天时间。','08:00-17:30','重庆市酉阳土家族苗族自治县桃花源街道'),

('金佛山','attraction','南川区','喀斯特,杜鹃,避暑','/images/spots/jinfoshan.jpg',
 '喀斯特台原与杜鹃花海，夏季避暑胜地。',
 '金佛山位于南川区，是世界自然遗产“中国南方喀斯特”的组成部分，以台原喀斯特地貌、古佛洞与高山杜鹃闻名。',
 60,90.00,'paid',0,'nature',0,
 '山顶温差大，夏季也需备外套；索道旺季排队较长。','08:00-17:00','重庆市南川区金佛山景区'),

('四面山','attraction','江津区','丹霞,瀑布,森林','/images/spots/simianshan.jpg',
 '丹霞瀑布群，望乡台瀑布飞流直下。',
 '四面山位于江津区，以丹霞地貌与瀑布群著称。望乡台瀑布落差约 150 米，四周森林茂密，夏季气温明显低于主城。',
 58,60.00,'paid',0,'nature',0,
 '景区范围较大，建议自驾或乘观光车串联各景点。','08:00-18:00','重庆市江津区四面山镇'),

('龚滩古镇','attraction','酉阳县','古镇,乌江,吊脚楼','/images/spots/gongtan.jpg',
 '乌江画廊边的千年古镇，吊脚楼临江而立。',
 '龚滩古镇位于酉阳县乌江与阿蓬江交汇处，青石板街与吊脚楼沿江分布，是乌江画廊的重要节点，也是土家族聚居的古镇。',
 56,0.00,'free',0,'history',0,
 '可先乘船游乌江画廊，再回古镇老街慢逛。','全天开放','重庆市酉阳土家族苗族自治县龚滩镇'),

('钓鱼城','attraction','合川区','古战场,宋城,历史','/images/spots/diaoyucheng.jpg',
 '改变世界历史的古战场，山城防御典范。',
 '钓鱼城位于合川区钓鱼山，南宋军民在此坚守三十六年。城址依山临江，城墙、炮台与暗道保存较多，是研究古代山城防御体系的重要遗址。',
 54,60.00,'paid',0,'history',0,
 '山上步道较多，建议预留 3 小时并穿轻便鞋。','08:30-17:30','重庆市合川区钓鱼城街道'),

('白帝城·瞿塘峡','attraction','奉节县','诗城,三峡,夔门','/images/spots/baidicheng.jpg',
 '诗城白帝，瞿塘峡口的长江胜景。',
 '白帝城位于奉节县瞿塘峡口，因刘备托孤与历代诗人题咏闻名。登城可远眺夔门，两岸绝壁夹江对峙，是长江三峡的标志性景观。',
 52,100.00,'paid',0,'history',0,
 '与瞿塘峡游船可组合游览，注意游船班次时间。','08:00-17:30','重庆市奉节县白帝镇'),

('缙云山','attraction','北碚区','森林,古道,云海','/images/spots/jinyunshan.jpg',
 '主城北面的绿色屏障，云海与古道相伴。',
 '缙云山位于北碚区，是国家级自然保护区，以亚热带常绿阔叶林与缙云寺闻名。山间步道古木参天，雨后常见云海。',
 50,15.00,'paid',0,'nature',0,
 '可从北碚城区乘缆车或徒步上山，雨后云海概率更高。','08:00-18:00','重庆市北碚区缙云山'),

('丰都名山','attraction','丰都县','民俗,古建,长江','/images/spots/fengdu.jpg',
 '长江边的名山古刹，民俗文化浓厚。',
 '丰都名山位于长江北岸，古建筑群依山而建，集中呈现巴渝地区的民俗与宗教文化。登顶可俯瞰长江与丰都县城。',
 48,100.00,'paid',0,'history',0,
 '游览以石阶为主，可乘索道省力，预留 3 小时。','08:00-17:30','重庆市丰都县名山街道'),

('长寿湖','attraction','长寿区','湖泊,骑行,露营','/images/spots/changshouhu.jpg',
 '湖汊纵横的人工湖，适合环湖慢行。',
 '长寿湖是西南地区较大的人工湖，湖中岛屿众多，湖岸线曲折。环湖公路与观景平台适合骑行、露营与周末休闲。',
 46,40.00,'paid',0,'nature',0,
 '环湖路程较长，建议自驾或骑行，注意防晒。','全天开放','重庆市长寿区长寿湖镇')
ON DUPLICATE KEY UPDATE
  `type` = VALUES(`type`),
  `region` = VALUES(`region`),
  `tags` = VALUES(`tags`),
  `cover` = VALUES(`cover`),
  `intro` = VALUES(`intro`),
  `description` = VALUES(`description`),
  `heat_index` = VALUES(`heat_index`),
  `avg_cost` = VALUES(`avg_cost`),
  `price_type` = VALUES(`price_type`),
  `is_classic` = VALUES(`is_classic`),
  `scene` = VALUES(`scene`),
  `activity_flag` = VALUES(`activity_flag`),
  `play_tip` = VALUES(`play_tip`),
  `open_time` = VALUES(`open_time`),
  `address` = VALUES(`address`);

INSERT INTO `heritage` (`name`, `level`, `category`, `cover`, `intro`, `inheritor`, `experience_spot`, `related_spot_id`) VALUES
('川剧', '国家级', '传统戏剧', '', '中国西南地区代表性剧种，以变脸、吐火、滚灯等绝技闻名，唱腔高亢激越。', '沈铁梅', '洪崖洞巴渝剧院', 11),
('重庆火锅传统制作技艺', '市级', '传统技艺', '', '牛油熬制、底料炒制、九宫格分格的独特技艺，承载重庆码头文化。', '王刚', '渝中区各老字号火锅店', 7),
('蜀绣', '国家级', '传统美术', '', '中国四大名绣之一，针法严谨、色彩明快，题材多为花鸟鱼虫。', '郝淑萍', '磁器口古镇绣坊', 3),
('木洞山歌', '国家级', '传统音乐', '', '巴南区木洞镇流传的山歌，高亢悠扬，被称为"巴渝文化的活化石"。', '潘中明', '巴南区木洞镇', NULL),
('梁平竹帘', '国家级', '传统美术', '', '以慈竹为原料手工编织的竹帘画，工艺精细，可书写绘画。', '牟秉衡', '梁平区竹帘厂', NULL),
('秀山花灯', '国家级', '传统舞蹈', '', '集歌、舞、戏于一体的民间灯舞，春节闹灯习俗流传数百年。', '杨正斌', '秀山县花灯广场', NULL);

INSERT INTO `hotel` (`name`, `region`, `price_range`, `intro`) VALUES
('洪崖洞大酒店', '渝中区', '豪华', '紧邻洪崖洞，房间可俯瞰嘉陵江夜景，步行可达解放碑。'),
('解放碑威斯汀酒店', '渝中区', '豪华', '位于解放碑商圈核心，高层客房可观赏城市全景，配套齐全。'),
('磁器口古镇客栈', '沙坪坝区', '经济', '古镇内特色客栈，青砖小院，性价比高，出门即是老街。'),
('南滨路江景酒店', '南岸区', '适中', '南滨路沿线，正对渝中半岛，夜景房视野开阔。'),
('观音桥商务酒店', '江北区', '经济', '观音桥商圈，地铁直达，周边餐饮购物便利，适合年轻旅客。'),
('武隆仙女山度假酒店', '武隆区', '适中', '靠近天生三桥景区，环境清幽，适合自驾与亲子出游。');
