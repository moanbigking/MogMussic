package com.example.moan.mogmussic.util;

public class Constant {
    public class Where {
        public static final String WHERE = "where";
        public static final String WHERE_CLICK_SONG = "song";
        public static final String WHERE_CLICK_LOCAL_SONG = "local_song";
        public static final String WHERE_CLICK_LOCAL_PLAY_ALL = "all";
        public static final String WHERE_CLICK_LIST_SONG = "list_song";
        public static final String WHERE_CLICK_LIST_PLAY_ALL = "list_all";
        public static final String WHERE_ONLINE = "mog.online";
    }

    public static final String MUSIC_CLICKED = "music_clicked";
    public static final String LIST_CLICKED = "list_clicked";
    public static final String MUSIC_SERVICE = "music_service";
    public static final String MUSIC_FORMAL_PLAY = "music_formal_play";
    public static final String DEFAULT_VALUE = "mog";
    public static final String MUSIC_LIST_CHOSEN = "mog.music_list_clicked";
    public static final String ONLINE_SONG_CLICKED = "mog.online_song_clicked";
    public static final String ONLINE_CACHE = "mog.online_cache";

    public class Action {
        public static final String ACTION_REFRESH_LIST = "mog.music.refresh_list";
        public static final String ACTION_START_MUSIC = "mog.music.start_music";
        public static final String ACTION_UPDATE_TIME = "mog.music.update_time";
        public static final String ACTION_SONG_FINISHED = "mog.music.song_finished";
        public static final String ACTION_BINDER_INIT = "mog.music.binder_init";
        public static final String ACTION_CONTROL_NOTIFICATION = "mog.music.control_notification";
        public static final String ACTION_FINISH = "mog.music.finish";
        public static final String ACTION_SET_VIEW = "mog.set_view";
    }

    public class Type {
        public static final int LOOPING = 0;
        public static final int ORDER = 1;
        public static final int RANDOM = 2;
        public static final String T_LOOPING = "单曲循环";
        public static final String T_ORDER = "列表播放";
        public static final String T_RANDOM = "随机播放";
    }

    public class Notification {
        public static final String CHANNEL_ID = "moanbigking.mussic";
        public static final int ID = 111;
        public static final String CHANNEL_NAME = "mogmussic.channel_name";
        public static final String CONTROL = "control";
        public static final int REQUEST_CONTROL = 222;
        public static final int REQUEST_NEXT = 333;
        public static final int REQUEST_PREVIOUS = 444;
        public static final int REQUEST_ENTER_ACTIVITY = 0;
        public static final String KEY = "key";
        public static final String KEY_CONTROL = "click_control";
        public static final String KEY_NEXT = "click_next";
        public static final String KEY_PREVIOUS = "click_previous";
    }

    public class Toast {
        public static final String CANNOT_DONE = "操作失败";
        public static final String DOUBLE_PRESS = "再按一次退出程序";
        public static final String WRONG_PASSWORD = "密码不正确";
        public static final String INPUT_INVALID = "没有找到";
    }

    public class Words {
        public static final String PERMITTING_OK = "确定";
        public static final String QUIT_CLOCK_MODE = "确定退出时钟模式？";
        public static final String PERMITTING_DENY = "取消";
        public static final String WARNING = "真的考虑好了吗？此操作不可撤销！";
    }

    public class Key {
        public static final String URL = "handler.base";
        public static final String LYRICS = "handler.lyrics";
        public static final String ONLINE = "mog.online";
    }
}