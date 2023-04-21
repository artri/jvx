/*
 * Copyright 2015 SIB Visions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 * History
 *
 * 30.09.2015 - [JR] - creation
 */
package com.sibvisions.rad.ui.swing.ext.fonts;

import java.util.HashMap;

/**
 * The <code>FontAwesome</code> class defines all supported font icons.
 * Currently, version 4.4.0 is supported.
 * 
 * @author René Jahn
 * @see <a href="http://fortawesome.github.io/Font-Awesome/cheatsheet/">http://fortawesome.github.io/Font-Awesome/cheatsheet/</a>
 */
public enum FontAwesome 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // enum definition 
    // (parsed with FontAwesomeParser.java - research package)
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** glass icon. */
    GLASS("glass", '\uf000'),
    /** music icon. */
    MUSIC("music", '\uf001'),
    /** search icon. */
    SEARCH("search", '\uf002'),
    /** envelope-o icon. */
    ENVELOPE_O("envelope-o", '\uf003'),
    /** heart icon. */
    HEART("heart", '\uf004'),
    /** star icon. */
    STAR("star", '\uf005'),
    /** star-o icon. */
    STAR_O("star-o", '\uf006'),
    /** user icon. */
    USER("user", '\uf007'),
    /** film icon. */
    FILM("film", '\uf008'),
    /** th-large icon. */
    TH_LARGE("th-large", '\uf009'),
    /** th icon. */
    TH("th", '\uf00a'),
    /** th-list icon. */
    TH_LIST("th-list", '\uf00b'),
    /** check icon. */
    CHECK("check", '\uf00c'),
    /** remove icon. */
    REMOVE("remove", '\uf00d'),
    /** close icon. */
    CLOSE("close", '\uf00d'),
    /** times icon. */
    TIMES("times", '\uf00d'),
    /** search-plus icon. */
    SEARCH_PLUS("search-plus", '\uf00e'),
    /** search-minus icon. */
    SEARCH_MINUS("search-minus", '\uf010'),
    /** power-off icon. */
    POWER_OFF("power-off", '\uf011'),
    /** signal icon. */
    SIGNAL("signal", '\uf012'),
    /** gear icon. */
    GEAR("gear", '\uf013'),
    /** cog icon. */
    COG("cog", '\uf013'),
    /** trash-o icon. */
    TRASH_O("trash-o", '\uf014'),
    /** home icon. */
    HOME("home", '\uf015'),
    /** file-o icon. */
    FILE_O("file-o", '\uf016'),
    /** clock-o icon. */
    CLOCK_O("clock-o", '\uf017'),
    /** road icon. */
    ROAD("road", '\uf018'),
    /** download icon. */
    DOWNLOAD("download", '\uf019'),
    /** arrow-circle-o-down icon. */
    ARROW_CIRCLE_O_DOWN("arrow-circle-o-down", '\uf01a'),
    /** arrow-circle-o-up icon. */
    ARROW_CIRCLE_O_UP("arrow-circle-o-up", '\uf01b'),
    /** inbox icon. */
    INBOX("inbox", '\uf01c'),
    /** play-circle-o icon. */
    PLAY_CIRCLE_O("play-circle-o", '\uf01d'),
    /** rotate-right icon. */
    ROTATE_RIGHT("rotate-right", '\uf01e'),
    /** repeat icon. */
    REPEAT("repeat", '\uf01e'),
    /** refresh icon. */
    REFRESH("refresh", '\uf021'),
    /** list-alt icon. */
    LIST_ALT("list-alt", '\uf022'),
    /** lock icon. */
    LOCK("lock", '\uf023'),
    /** flag icon. */
    FLAG("flag", '\uf024'),
    /** headphones icon. */
    HEADPHONES("headphones", '\uf025'),
    /** volume-off icon. */
    VOLUME_OFF("volume-off", '\uf026'),
    /** volume-down icon. */
    VOLUME_DOWN("volume-down", '\uf027'),
    /** volume-up icon. */
    VOLUME_UP("volume-up", '\uf028'),
    /** qrcode icon. */
    QRCODE("qrcode", '\uf029'),
    /** barcode icon. */
    BARCODE("barcode", '\uf02a'),
    /** tag icon. */
    TAG("tag", '\uf02b'),
    /** tags icon. */
    TAGS("tags", '\uf02c'),
    /** book icon. */
    BOOK("book", '\uf02d'),
    /** bookmark icon. */
    BOOKMARK("bookmark", '\uf02e'),
    /** print icon. */
    PRINT("print", '\uf02f'),
    /** camera icon. */
    CAMERA("camera", '\uf030'),
    /** font icon. */
    FONT("font", '\uf031'),
    /** bold icon. */
    BOLD("bold", '\uf032'),
    /** italic icon. */
    ITALIC("italic", '\uf033'),
    /** text-height icon. */
    TEXT_HEIGHT("text-height", '\uf034'),
    /** text-width icon. */
    TEXT_WIDTH("text-width", '\uf035'),
    /** align-left icon. */
    ALIGN_LEFT("align-left", '\uf036'),
    /** align-center icon. */
    ALIGN_CENTER("align-center", '\uf037'),
    /** align-right icon. */
    ALIGN_RIGHT("align-right", '\uf038'),
    /** align-justify icon. */
    ALIGN_JUSTIFY("align-justify", '\uf039'),
    /** list icon. */
    LIST("list", '\uf03a'),
    /** dedent icon. */
    DEDENT("dedent", '\uf03b'),
    /** outdent icon. */
    OUTDENT("outdent", '\uf03b'),
    /** indent icon. */
    INDENT("indent", '\uf03c'),
    /** video-camera icon. */
    VIDEO_CAMERA("video-camera", '\uf03d'),
    /** photo icon. */
    PHOTO("photo", '\uf03e'),
    /** image icon. */
    IMAGE("image", '\uf03e'),
    /** picture-o icon. */
    PICTURE_O("picture-o", '\uf03e'),
    /** pencil icon. */
    PENCIL("pencil", '\uf040'),
    /** map-marker icon. */
    MAP_MARKER("map-marker", '\uf041'),
    /** adjust icon. */
    ADJUST("adjust", '\uf042'),
    /** tint icon. */
    TINT("tint", '\uf043'),
    /** edit icon. */
    EDIT("edit", '\uf044'),
    /** pencil-square-o icon. */
    PENCIL_SQUARE_O("pencil-square-o", '\uf044'),
    /** share-square-o icon. */
    SHARE_SQUARE_O("share-square-o", '\uf045'),
    /** check-square-o icon. */
    CHECK_SQUARE_O("check-square-o", '\uf046'),
    /** arrows icon. */
    ARROWS("arrows", '\uf047'),
    /** step-backward icon. */
    STEP_BACKWARD("step-backward", '\uf048'),
    /** fast-backward icon. */
    FAST_BACKWARD("fast-backward", '\uf049'),
    /** backward icon. */
    BACKWARD("backward", '\uf04a'),
    /** play icon. */
    PLAY("play", '\uf04b'),
    /** pause icon. */
    PAUSE("pause", '\uf04c'),
    /** stop icon. */
    STOP("stop", '\uf04d'),
    /** forward icon. */
    FORWARD("forward", '\uf04e'),
    /** fast-forward icon. */
    FAST_FORWARD("fast-forward", '\uf050'),
    /** step-forward icon. */
    STEP_FORWARD("step-forward", '\uf051'),
    /** eject icon. */
    EJECT("eject", '\uf052'),
    /** chevron-left icon. */
    CHEVRON_LEFT("chevron-left", '\uf053'),
    /** chevron-right icon. */
    CHEVRON_RIGHT("chevron-right", '\uf054'),
    /** plus-circle icon. */
    PLUS_CIRCLE("plus-circle", '\uf055'),
    /** minus-circle icon. */
    MINUS_CIRCLE("minus-circle", '\uf056'),
    /** times-circle icon. */
    TIMES_CIRCLE("times-circle", '\uf057'),
    /** check-circle icon. */
    CHECK_CIRCLE("check-circle", '\uf058'),
    /** question-circle icon. */
    QUESTION_CIRCLE("question-circle", '\uf059'),
    /** info-circle icon. */
    INFO_CIRCLE("info-circle", '\uf05a'),
    /** crosshairs icon. */
    CROSSHAIRS("crosshairs", '\uf05b'),
    /** times-circle-o icon. */
    TIMES_CIRCLE_O("times-circle-o", '\uf05c'),
    /** check-circle-o icon. */
    CHECK_CIRCLE_O("check-circle-o", '\uf05d'),
    /** ban icon. */
    BAN("ban", '\uf05e'),
    /** arrow-left icon. */
    ARROW_LEFT("arrow-left", '\uf060'),
    /** arrow-right icon. */
    ARROW_RIGHT("arrow-right", '\uf061'),
    /** arrow-up icon. */
    ARROW_UP("arrow-up", '\uf062'),
    /** arrow-down icon. */
    ARROW_DOWN("arrow-down", '\uf063'),
    /** mail-forward icon. */
    MAIL_FORWARD("mail-forward", '\uf064'),
    /** share icon. */
    SHARE("share", '\uf064'),
    /** expand icon. */
    EXPAND("expand", '\uf065'),
    /** compress icon. */
    COMPRESS("compress", '\uf066'),
    /** plus icon. */
    PLUS("plus", '\uf067'),
    /** minus icon. */
    MINUS("minus", '\uf068'),
    /** asterisk icon. */
    ASTERISK("asterisk", '\uf069'),
    /** exclamation-circle icon. */
    EXCLAMATION_CIRCLE("exclamation-circle", '\uf06a'),
    /** gift icon. */
    GIFT("gift", '\uf06b'),
    /** leaf icon. */
    LEAF("leaf", '\uf06c'),
    /** fire icon. */
    FIRE("fire", '\uf06d'),
    /** eye icon. */
    EYE("eye", '\uf06e'),
    /** eye-slash icon. */
    EYE_SLASH("eye-slash", '\uf070'),
    /** warning icon. */
    WARNING("warning", '\uf071'),
    /** exclamation-triangle icon. */
    EXCLAMATION_TRIANGLE("exclamation-triangle", '\uf071'),
    /** plane icon. */
    PLANE("plane", '\uf072'),
    /** calendar icon. */
    CALENDAR("calendar", '\uf073'),
    /** random icon. */
    RANDOM("random", '\uf074'),
    /** comment icon. */
    COMMENT("comment", '\uf075'),
    /** magnet icon. */
    MAGNET("magnet", '\uf076'),
    /** chevron-up icon. */
    CHEVRON_UP("chevron-up", '\uf077'),
    /** chevron-down icon. */
    CHEVRON_DOWN("chevron-down", '\uf078'),
    /** retweet icon. */
    RETWEET("retweet", '\uf079'),
    /** shopping-cart icon. */
    SHOPPING_CART("shopping-cart", '\uf07a'),
    /** folder icon. */
    FOLDER("folder", '\uf07b'),
    /** folder-open icon. */
    FOLDER_OPEN("folder-open", '\uf07c'),
    /** arrows-v icon. */
    ARROWS_V("arrows-v", '\uf07d'),
    /** arrows-h icon. */
    ARROWS_H("arrows-h", '\uf07e'),
    /** bar-chart-o icon. */
    BAR_CHART_O("bar-chart-o", '\uf080'),
    /** bar-chart icon. */
    BAR_CHART("bar-chart", '\uf080'),
    /** twitter-square icon. */
    TWITTER_SQUARE("twitter-square", '\uf081'),
    /** facebook-square icon. */
    FACEBOOK_SQUARE("facebook-square", '\uf082'),
    /** camera-retro icon. */
    CAMERA_RETRO("camera-retro", '\uf083'),
    /** key icon. */
    KEY("key", '\uf084'),
    /** gears icon. */
    GEARS("gears", '\uf085'),
    /** cogs icon. */
    COGS("cogs", '\uf085'),
    /** comments icon. */
    COMMENTS("comments", '\uf086'),
    /** thumbs-o-up icon. */
    THUMBS_O_UP("thumbs-o-up", '\uf087'),
    /** thumbs-o-down icon. */
    THUMBS_O_DOWN("thumbs-o-down", '\uf088'),
    /** star-half icon. */
    STAR_HALF("star-half", '\uf089'),
    /** heart-o icon. */
    HEART_O("heart-o", '\uf08a'),
    /** sign-out icon. */
    SIGN_OUT("sign-out", '\uf08b'),
    /** linkedin-square icon. */
    LINKEDIN_SQUARE("linkedin-square", '\uf08c'),
    /** thumb-tack icon. */
    THUMB_TACK("thumb-tack", '\uf08d'),
    /** external-link icon. */
    EXTERNAL_LINK("external-link", '\uf08e'),
    /** sign-in icon. */
    SIGN_IN("sign-in", '\uf090'),
    /** trophy icon. */
    TROPHY("trophy", '\uf091'),
    /** github-square icon. */
    GITHUB_SQUARE("github-square", '\uf092'),
    /** upload icon. */
    UPLOAD("upload", '\uf093'),
    /** lemon-o icon. */
    LEMON_O("lemon-o", '\uf094'),
    /** phone icon. */
    PHONE("phone", '\uf095'),
    /** square-o icon. */
    SQUARE_O("square-o", '\uf096'),
    /** bookmark-o icon. */
    BOOKMARK_O("bookmark-o", '\uf097'),
    /** phone-square icon. */
    PHONE_SQUARE("phone-square", '\uf098'),
    /** twitter icon. */
    TWITTER("twitter", '\uf099'),
    /** facebook-f icon. */
    FACEBOOK_F("facebook-f", '\uf09a'),
    /** facebook icon. */
    FACEBOOK("facebook", '\uf09a'),
    /** github icon. */
    GITHUB("github", '\uf09b'),
    /** unlock icon. */
    UNLOCK("unlock", '\uf09c'),
    /** credit-card icon. */
    CREDIT_CARD("credit-card", '\uf09d'),
    /** feed icon. */
    FEED("feed", '\uf09e'),
    /** rss icon. */
    RSS("rss", '\uf09e'),
    /** hdd-o icon. */
    HDD_O("hdd-o", '\uf0a0'),
    /** bullhorn icon. */
    BULLHORN("bullhorn", '\uf0a1'),
    /** bell icon. */
    BELL("bell", '\uf0f3'),
    /** certificate icon. */
    CERTIFICATE("certificate", '\uf0a3'),
    /** hand-o-right icon. */
    HAND_O_RIGHT("hand-o-right", '\uf0a4'),
    /** hand-o-left icon. */
    HAND_O_LEFT("hand-o-left", '\uf0a5'),
    /** hand-o-up icon. */
    HAND_O_UP("hand-o-up", '\uf0a6'),
    /** hand-o-down icon. */
    HAND_O_DOWN("hand-o-down", '\uf0a7'),
    /** arrow-circle-left icon. */
    ARROW_CIRCLE_LEFT("arrow-circle-left", '\uf0a8'),
    /** arrow-circle-right icon. */
    ARROW_CIRCLE_RIGHT("arrow-circle-right", '\uf0a9'),
    /** arrow-circle-up icon. */
    ARROW_CIRCLE_UP("arrow-circle-up", '\uf0aa'),
    /** arrow-circle-down icon. */
    ARROW_CIRCLE_DOWN("arrow-circle-down", '\uf0ab'),
    /** globe icon. */
    GLOBE("globe", '\uf0ac'),
    /** wrench icon. */
    WRENCH("wrench", '\uf0ad'),
    /** tasks icon. */
    TASKS("tasks", '\uf0ae'),
    /** filter icon. */
    FILTER("filter", '\uf0b0'),
    /** briefcase icon. */
    BRIEFCASE("briefcase", '\uf0b1'),
    /** arrows-alt icon. */
    ARROWS_ALT("arrows-alt", '\uf0b2'),
    /** group icon. */
    GROUP("group", '\uf0c0'),
    /** users icon. */
    USERS("users", '\uf0c0'),
    /** chain icon. */
    CHAIN("chain", '\uf0c1'),
    /** link icon. */
    LINK("link", '\uf0c1'),
    /** cloud icon. */
    CLOUD("cloud", '\uf0c2'),
    /** flask icon. */
    FLASK("flask", '\uf0c3'),
    /** cut icon. */
    CUT("cut", '\uf0c4'),
    /** scissors icon. */
    SCISSORS("scissors", '\uf0c4'),
    /** copy icon. */
    COPY("copy", '\uf0c5'),
    /** files-o icon. */
    FILES_O("files-o", '\uf0c5'),
    /** paperclip icon. */
    PAPERCLIP("paperclip", '\uf0c6'),
    /** save icon. */
    SAVE("save", '\uf0c7'),
    /** floppy-o icon. */
    FLOPPY_O("floppy-o", '\uf0c7'),
    /** square icon. */
    SQUARE("square", '\uf0c8'),
    /** navicon icon. */
    NAVICON("navicon", '\uf0c9'),
    /** reorder icon. */
    REORDER("reorder", '\uf0c9'),
    /** bars icon. */
    BARS("bars", '\uf0c9'),
    /** list-ul icon. */
    LIST_UL("list-ul", '\uf0ca'),
    /** list-ol icon. */
    LIST_OL("list-ol", '\uf0cb'),
    /** strikethrough icon. */
    STRIKETHROUGH("strikethrough", '\uf0cc'),
    /** underline icon. */
    UNDERLINE("underline", '\uf0cd'),
    /** table icon. */
    TABLE("table", '\uf0ce'),
    /** magic icon. */
    MAGIC("magic", '\uf0d0'),
    /** truck icon. */
    TRUCK("truck", '\uf0d1'),
    /** pinterest icon. */
    PINTEREST("pinterest", '\uf0d2'),
    /** pinterest-square icon. */
    PINTEREST_SQUARE("pinterest-square", '\uf0d3'),
    /** google-plus-square icon. */
    GOOGLE_PLUS_SQUARE("google-plus-square", '\uf0d4'),
    /** google-plus icon. */
    GOOGLE_PLUS("google-plus", '\uf0d5'),
    /** money icon. */
    MONEY("money", '\uf0d6'),
    /** caret-down icon. */
    CARET_DOWN("caret-down", '\uf0d7'),
    /** caret-up icon. */
    CARET_UP("caret-up", '\uf0d8'),
    /** caret-left icon. */
    CARET_LEFT("caret-left", '\uf0d9'),
    /** caret-right icon. */
    CARET_RIGHT("caret-right", '\uf0da'),
    /** columns icon. */
    COLUMNS("columns", '\uf0db'),
    /** unsorted icon. */
    UNSORTED("unsorted", '\uf0dc'),
    /** sort icon. */
    SORT("sort", '\uf0dc'),
    /** sort-down icon. */
    SORT_DOWN("sort-down", '\uf0dd'),
    /** sort-desc icon. */
    SORT_DESC("sort-desc", '\uf0dd'),
    /** sort-up icon. */
    SORT_UP("sort-up", '\uf0de'),
    /** sort-asc icon. */
    SORT_ASC("sort-asc", '\uf0de'),
    /** envelope icon. */
    ENVELOPE("envelope", '\uf0e0'),
    /** linkedin icon. */
    LINKEDIN("linkedin", '\uf0e1'),
    /** rotate-left icon. */
    ROTATE_LEFT("rotate-left", '\uf0e2'),
    /** undo icon. */
    UNDO("undo", '\uf0e2'),
    /** legal icon. */
    LEGAL("legal", '\uf0e3'),
    /** gavel icon. */
    GAVEL("gavel", '\uf0e3'),
    /** dashboard icon. */
    DASHBOARD("dashboard", '\uf0e4'),
    /** tachometer icon. */
    TACHOMETER("tachometer", '\uf0e4'),
    /** comment-o icon. */
    COMMENT_O("comment-o", '\uf0e5'),
    /** comments-o icon. */
    COMMENTS_O("comments-o", '\uf0e6'),
    /** flash icon. */
    FLASH("flash", '\uf0e7'),
    /** bolt icon. */
    BOLT("bolt", '\uf0e7'),
    /** sitemap icon. */
    SITEMAP("sitemap", '\uf0e8'),
    /** umbrella icon. */
    UMBRELLA("umbrella", '\uf0e9'),
    /** paste icon. */
    PASTE("paste", '\uf0ea'),
    /** clipboard icon. */
    CLIPBOARD("clipboard", '\uf0ea'),
    /** lightbulb-o icon. */
    LIGHTBULB_O("lightbulb-o", '\uf0eb'),
    /** exchange icon. */
    EXCHANGE("exchange", '\uf0ec'),
    /** cloud-download icon. */
    CLOUD_DOWNLOAD("cloud-download", '\uf0ed'),
    /** cloud-upload icon. */
    CLOUD_UPLOAD("cloud-upload", '\uf0ee'),
    /** user-md icon. */
    USER_MD("user-md", '\uf0f0'),
    /** stethoscope icon. */
    STETHOSCOPE("stethoscope", '\uf0f1'),
    /** suitcase icon. */
    SUITCASE("suitcase", '\uf0f2'),
    /** bell-o icon. */
    BELL_O("bell-o", '\uf0a2'),
    /** coffee icon. */
    COFFEE("coffee", '\uf0f4'),
    /** cutlery icon. */
    CUTLERY("cutlery", '\uf0f5'),
    /** file-text-o icon. */
    FILE_TEXT_O("file-text-o", '\uf0f6'),
    /** building-o icon. */
    BUILDING_O("building-o", '\uf0f7'),
    /** hospital-o icon. */
    HOSPITAL_O("hospital-o", '\uf0f8'),
    /** ambulance icon. */
    AMBULANCE("ambulance", '\uf0f9'),
    /** medkit icon. */
    MEDKIT("medkit", '\uf0fa'),
    /** fighter-jet icon. */
    FIGHTER_JET("fighter-jet", '\uf0fb'),
    /** beer icon. */
    BEER("beer", '\uf0fc'),
    /** h-square icon. */
    H_SQUARE("h-square", '\uf0fd'),
    /** plus-square icon. */
    PLUS_SQUARE("plus-square", '\uf0fe'),
    /** angle-double-left icon. */
    ANGLE_DOUBLE_LEFT("angle-double-left", '\uf100'),
    /** angle-double-right icon. */
    ANGLE_DOUBLE_RIGHT("angle-double-right", '\uf101'),
    /** angle-double-up icon. */
    ANGLE_DOUBLE_UP("angle-double-up", '\uf102'),
    /** angle-double-down icon. */
    ANGLE_DOUBLE_DOWN("angle-double-down", '\uf103'),
    /** angle-left icon. */
    ANGLE_LEFT("angle-left", '\uf104'),
    /** angle-right icon. */
    ANGLE_RIGHT("angle-right", '\uf105'),
    /** angle-up icon. */
    ANGLE_UP("angle-up", '\uf106'),
    /** angle-down icon. */
    ANGLE_DOWN("angle-down", '\uf107'),
    /** desktop icon. */
    DESKTOP("desktop", '\uf108'),
    /** laptop icon. */
    LAPTOP("laptop", '\uf109'),
    /** tablet icon. */
    TABLET("tablet", '\uf10a'),
    /** mobile-phone icon. */
    MOBILE_PHONE("mobile-phone", '\uf10b'),
    /** mobile icon. */
    MOBILE("mobile", '\uf10b'),
    /** circle-o icon. */
    CIRCLE_O("circle-o", '\uf10c'),
    /** quote-left icon. */
    QUOTE_LEFT("quote-left", '\uf10d'),
    /** quote-right icon. */
    QUOTE_RIGHT("quote-right", '\uf10e'),
    /** spinner icon. */
    SPINNER("spinner", '\uf110'),
    /** circle icon. */
    CIRCLE("circle", '\uf111'),
    /** mail-reply icon. */
    MAIL_REPLY("mail-reply", '\uf112'),
    /** reply icon. */
    REPLY("reply", '\uf112'),
    /** github-alt icon. */
    GITHUB_ALT("github-alt", '\uf113'),
    /** folder-o icon. */
    FOLDER_O("folder-o", '\uf114'),
    /** folder-open-o icon. */
    FOLDER_OPEN_O("folder-open-o", '\uf115'),
    /** smile-o icon. */
    SMILE_O("smile-o", '\uf118'),
    /** frown-o icon. */
    FROWN_O("frown-o", '\uf119'),
    /** meh-o icon. */
    MEH_O("meh-o", '\uf11a'),
    /** gamepad icon. */
    GAMEPAD("gamepad", '\uf11b'),
    /** keyboard-o icon. */
    KEYBOARD_O("keyboard-o", '\uf11c'),
    /** flag-o icon. */
    FLAG_O("flag-o", '\uf11d'),
    /** flag-checkered icon. */
    FLAG_CHECKERED("flag-checkered", '\uf11e'),
    /** terminal icon. */
    TERMINAL("terminal", '\uf120'),
    /** code icon. */
    CODE("code", '\uf121'),
    /** mail-reply-all icon. */
    MAIL_REPLY_ALL("mail-reply-all", '\uf122'),
    /** reply-all icon. */
    REPLY_ALL("reply-all", '\uf122'),
    /** star-half-empty icon. */
    STAR_HALF_EMPTY("star-half-empty", '\uf123'),
    /** star-half-full icon. */
    STAR_HALF_FULL("star-half-full", '\uf123'),
    /** star-half-o icon. */
    STAR_HALF_O("star-half-o", '\uf123'),
    /** location-arrow icon. */
    LOCATION_ARROW("location-arrow", '\uf124'),
    /** crop icon. */
    CROP("crop", '\uf125'),
    /** code-fork icon. */
    CODE_FORK("code-fork", '\uf126'),
    /** unlink icon. */
    UNLINK("unlink", '\uf127'),
    /** chain-broken icon. */
    CHAIN_BROKEN("chain-broken", '\uf127'),
    /** question icon. */
    QUESTION("question", '\uf128'),
    /** info icon. */
    INFO("info", '\uf129'),
    /** exclamation icon. */
    EXCLAMATION("exclamation", '\uf12a'),
    /** superscript icon. */
    SUPERSCRIPT("superscript", '\uf12b'),
    /** subscript icon. */
    SUBSCRIPT("subscript", '\uf12c'),
    /** eraser icon. */
    ERASER("eraser", '\uf12d'),
    /** puzzle-piece icon. */
    PUZZLE_PIECE("puzzle-piece", '\uf12e'),
    /** microphone icon. */
    MICROPHONE("microphone", '\uf130'),
    /** microphone-slash icon. */
    MICROPHONE_SLASH("microphone-slash", '\uf131'),
    /** shield icon. */
    SHIELD("shield", '\uf132'),
    /** calendar-o icon. */
    CALENDAR_O("calendar-o", '\uf133'),
    /** fire-extinguisher icon. */
    FIRE_EXTINGUISHER("fire-extinguisher", '\uf134'),
    /** rocket icon. */
    ROCKET("rocket", '\uf135'),
    /** maxcdn icon. */
    MAXCDN("maxcdn", '\uf136'),
    /** chevron-circle-left icon. */
    CHEVRON_CIRCLE_LEFT("chevron-circle-left", '\uf137'),
    /** chevron-circle-right icon. */
    CHEVRON_CIRCLE_RIGHT("chevron-circle-right", '\uf138'),
    /** chevron-circle-up icon. */
    CHEVRON_CIRCLE_UP("chevron-circle-up", '\uf139'),
    /** chevron-circle-down icon. */
    CHEVRON_CIRCLE_DOWN("chevron-circle-down", '\uf13a'),
    /** html5 icon. */
    HTML5("html5", '\uf13b'),
    /** css3 icon. */
    CSS3("css3", '\uf13c'),
    /** anchor icon. */
    ANCHOR("anchor", '\uf13d'),
    /** unlock-alt icon. */
    UNLOCK_ALT("unlock-alt", '\uf13e'),
    /** bullseye icon. */
    BULLSEYE("bullseye", '\uf140'),
    /** ellipsis-h icon. */
    ELLIPSIS_H("ellipsis-h", '\uf141'),
    /** ellipsis-v icon. */
    ELLIPSIS_V("ellipsis-v", '\uf142'),
    /** rss-square icon. */
    RSS_SQUARE("rss-square", '\uf143'),
    /** play-circle icon. */
    PLAY_CIRCLE("play-circle", '\uf144'),
    /** ticket icon. */
    TICKET("ticket", '\uf145'),
    /** minus-square icon. */
    MINUS_SQUARE("minus-square", '\uf146'),
    /** minus-square-o icon. */
    MINUS_SQUARE_O("minus-square-o", '\uf147'),
    /** level-up icon. */
    LEVEL_UP("level-up", '\uf148'),
    /** level-down icon. */
    LEVEL_DOWN("level-down", '\uf149'),
    /** check-square icon. */
    CHECK_SQUARE("check-square", '\uf14a'),
    /** pencil-square icon. */
    PENCIL_SQUARE("pencil-square", '\uf14b'),
    /** external-link-square icon. */
    EXTERNAL_LINK_SQUARE("external-link-square", '\uf14c'),
    /** share-square icon. */
    SHARE_SQUARE("share-square", '\uf14d'),
    /** compass icon. */
    COMPASS("compass", '\uf14e'),
    /** toggle-down icon. */
    TOGGLE_DOWN("toggle-down", '\uf150'),
    /** caret-square-o-down icon. */
    CARET_SQUARE_O_DOWN("caret-square-o-down", '\uf150'),
    /** toggle-up icon. */
    TOGGLE_UP("toggle-up", '\uf151'),
    /** caret-square-o-up icon. */
    CARET_SQUARE_O_UP("caret-square-o-up", '\uf151'),
    /** toggle-right icon. */
    TOGGLE_RIGHT("toggle-right", '\uf152'),
    /** caret-square-o-right icon. */
    CARET_SQUARE_O_RIGHT("caret-square-o-right", '\uf152'),
    /** euro icon. */
    EURO("euro", '\uf153'),
    /** eur icon. */
    EUR("eur", '\uf153'),
    /** gbp icon. */
    GBP("gbp", '\uf154'),
    /** dollar icon. */
    DOLLAR("dollar", '\uf155'),
    /** usd icon. */
    USD("usd", '\uf155'),
    /** rupee icon. */
    RUPEE("rupee", '\uf156'),
    /** inr icon. */
    INR("inr", '\uf156'),
    /** cny icon. */
    CNY("cny", '\uf157'),
    /** rmb icon. */
    RMB("rmb", '\uf157'),
    /** yen icon. */
    YEN("yen", '\uf157'),
    /** jpy icon. */
    JPY("jpy", '\uf157'),
    /** ruble icon. */
    RUBLE("ruble", '\uf158'),
    /** rouble icon. */
    ROUBLE("rouble", '\uf158'),
    /** rub icon. */
    RUB("rub", '\uf158'),
    /** won icon. */
    WON("won", '\uf159'),
    /** krw icon. */
    KRW("krw", '\uf159'),
    /** bitcoin icon. */
    BITCOIN("bitcoin", '\uf15a'),
    /** btc icon. */
    BTC("btc", '\uf15a'),
    /** file icon. */
    FILE("file", '\uf15b'),
    /** file-text icon. */
    FILE_TEXT("file-text", '\uf15c'),
    /** sort-alpha-asc icon. */
    SORT_ALPHA_ASC("sort-alpha-asc", '\uf15d'),
    /** sort-alpha-desc icon. */
    SORT_ALPHA_DESC("sort-alpha-desc", '\uf15e'),
    /** sort-amount-asc icon. */
    SORT_AMOUNT_ASC("sort-amount-asc", '\uf160'),
    /** sort-amount-desc icon. */
    SORT_AMOUNT_DESC("sort-amount-desc", '\uf161'),
    /** sort-numeric-asc icon. */
    SORT_NUMERIC_ASC("sort-numeric-asc", '\uf162'),
    /** sort-numeric-desc icon. */
    SORT_NUMERIC_DESC("sort-numeric-desc", '\uf163'),
    /** thumbs-up icon. */
    THUMBS_UP("thumbs-up", '\uf164'),
    /** thumbs-down icon. */
    THUMBS_DOWN("thumbs-down", '\uf165'),
    /** youtube-square icon. */
    YOUTUBE_SQUARE("youtube-square", '\uf166'),
    /** youtube icon. */
    YOUTUBE("youtube", '\uf167'),
    /** xing icon. */
    XING("xing", '\uf168'),
    /** xing-square icon. */
    XING_SQUARE("xing-square", '\uf169'),
    /** youtube-play icon. */
    YOUTUBE_PLAY("youtube-play", '\uf16a'),
    /** dropbox icon. */
    DROPBOX("dropbox", '\uf16b'),
    /** stack-overflow icon. */
    STACK_OVERFLOW("stack-overflow", '\uf16c'),
    /** instagram icon. */
    INSTAGRAM("instagram", '\uf16d'),
    /** flickr icon. */
    FLICKR("flickr", '\uf16e'),
    /** adn icon. */
    ADN("adn", '\uf170'),
    /** bitbucket icon. */
    BITBUCKET("bitbucket", '\uf171'),
    /** bitbucket-square icon. */
    BITBUCKET_SQUARE("bitbucket-square", '\uf172'),
    /** tumblr icon. */
    TUMBLR("tumblr", '\uf173'),
    /** tumblr-square icon. */
    TUMBLR_SQUARE("tumblr-square", '\uf174'),
    /** long-arrow-down icon. */
    LONG_ARROW_DOWN("long-arrow-down", '\uf175'),
    /** long-arrow-up icon. */
    LONG_ARROW_UP("long-arrow-up", '\uf176'),
    /** long-arrow-left icon. */
    LONG_ARROW_LEFT("long-arrow-left", '\uf177'),
    /** long-arrow-right icon. */
    LONG_ARROW_RIGHT("long-arrow-right", '\uf178'),
    /** apple icon. */
    APPLE("apple", '\uf179'),
    /** windows icon. */
    WINDOWS("windows", '\uf17a'),
    /** android icon. */
    ANDROID("android", '\uf17b'),
    /** linux icon. */
    LINUX("linux", '\uf17c'),
    /** dribbble icon. */
    DRIBBBLE("dribbble", '\uf17d'),
    /** skype icon. */
    SKYPE("skype", '\uf17e'),
    /** foursquare icon. */
    FOURSQUARE("foursquare", '\uf180'),
    /** trello icon. */
    TRELLO("trello", '\uf181'),
    /** female icon. */
    FEMALE("female", '\uf182'),
    /** male icon. */
    MALE("male", '\uf183'),
    /** gittip icon. */
    GITTIP("gittip", '\uf184'),
    /** gratipay icon. */
    GRATIPAY("gratipay", '\uf184'),
    /** sun-o icon. */
    SUN_O("sun-o", '\uf185'),
    /** moon-o icon. */
    MOON_O("moon-o", '\uf186'),
    /** archive icon. */
    ARCHIVE("archive", '\uf187'),
    /** bug icon. */
    BUG("bug", '\uf188'),
    /** vk icon. */
    VK("vk", '\uf189'),
    /** weibo icon. */
    WEIBO("weibo", '\uf18a'),
    /** renren icon. */
    RENREN("renren", '\uf18b'),
    /** pagelines icon. */
    PAGELINES("pagelines", '\uf18c'),
    /** stack-exchange icon. */
    STACK_EXCHANGE("stack-exchange", '\uf18d'),
    /** arrow-circle-o-right icon. */
    ARROW_CIRCLE_O_RIGHT("arrow-circle-o-right", '\uf18e'),
    /** arrow-circle-o-left icon. */
    ARROW_CIRCLE_O_LEFT("arrow-circle-o-left", '\uf190'),
    /** toggle-left icon. */
    TOGGLE_LEFT("toggle-left", '\uf191'),
    /** caret-square-o-left icon. */
    CARET_SQUARE_O_LEFT("caret-square-o-left", '\uf191'),
    /** dot-circle-o icon. */
    DOT_CIRCLE_O("dot-circle-o", '\uf192'),
    /** wheelchair icon. */
    WHEELCHAIR("wheelchair", '\uf193'),
    /** vimeo-square icon. */
    VIMEO_SQUARE("vimeo-square", '\uf194'),
    /** turkish-lira icon. */
    TURKISH_LIRA("turkish-lira", '\uf195'),
    /** try icon. */
    TRY("try", '\uf195'),
    /** plus-square-o icon. */
    PLUS_SQUARE_O("plus-square-o", '\uf196'),
    /** space-shuttle icon. */
    SPACE_SHUTTLE("space-shuttle", '\uf197'),
    /** slack icon. */
    SLACK("slack", '\uf198'),
    /** envelope-square icon. */
    ENVELOPE_SQUARE("envelope-square", '\uf199'),
    /** wordpress icon. */
    WORDPRESS("wordpress", '\uf19a'),
    /** openid icon. */
    OPENID("openid", '\uf19b'),
    /** institution icon. */
    INSTITUTION("institution", '\uf19c'),
    /** bank icon. */
    BANK("bank", '\uf19c'),
    /** university icon. */
    UNIVERSITY("university", '\uf19c'),
    /** mortar-board icon. */
    MORTAR_BOARD("mortar-board", '\uf19d'),
    /** graduation-cap icon. */
    GRADUATION_CAP("graduation-cap", '\uf19d'),
    /** yahoo icon. */
    YAHOO("yahoo", '\uf19e'),
    /** google icon. */
    GOOGLE("google", '\uf1a0'),
    /** reddit icon. */
    REDDIT("reddit", '\uf1a1'),
    /** reddit-square icon. */
    REDDIT_SQUARE("reddit-square", '\uf1a2'),
    /** stumbleupon-circle icon. */
    STUMBLEUPON_CIRCLE("stumbleupon-circle", '\uf1a3'),
    /** stumbleupon icon. */
    STUMBLEUPON("stumbleupon", '\uf1a4'),
    /** delicious icon. */
    DELICIOUS("delicious", '\uf1a5'),
    /** digg icon. */
    DIGG("digg", '\uf1a6'),
    /** pied-piper icon. */
    PIED_PIPER("pied-piper", '\uf1a7'),
    /** pied-piper-alt icon. */
    PIED_PIPER_ALT("pied-piper-alt", '\uf1a8'),
    /** drupal icon. */
    DRUPAL("drupal", '\uf1a9'),
    /** joomla icon. */
    JOOMLA("joomla", '\uf1aa'),
    /** language icon. */
    LANGUAGE("language", '\uf1ab'),
    /** fax icon. */
    FAX("fax", '\uf1ac'),
    /** building icon. */
    BUILDING("building", '\uf1ad'),
    /** child icon. */
    CHILD("child", '\uf1ae'),
    /** paw icon. */
    PAW("paw", '\uf1b0'),
    /** spoon icon. */
    SPOON("spoon", '\uf1b1'),
    /** cube icon. */
    CUBE("cube", '\uf1b2'),
    /** cubes icon. */
    CUBES("cubes", '\uf1b3'),
    /** behance icon. */
    BEHANCE("behance", '\uf1b4'),
    /** behance-square icon. */
    BEHANCE_SQUARE("behance-square", '\uf1b5'),
    /** steam icon. */
    STEAM("steam", '\uf1b6'),
    /** steam-square icon. */
    STEAM_SQUARE("steam-square", '\uf1b7'),
    /** recycle icon. */
    RECYCLE("recycle", '\uf1b8'),
    /** automobile icon. */
    AUTOMOBILE("automobile", '\uf1b9'),
    /** car icon. */
    CAR("car", '\uf1b9'),
    /** cab icon. */
    CAB("cab", '\uf1ba'),
    /** taxi icon. */
    TAXI("taxi", '\uf1ba'),
    /** tree icon. */
    TREE("tree", '\uf1bb'),
    /** spotify icon. */
    SPOTIFY("spotify", '\uf1bc'),
    /** deviantart icon. */
    DEVIANTART("deviantart", '\uf1bd'),
    /** soundcloud icon. */
    SOUNDCLOUD("soundcloud", '\uf1be'),
    /** database icon. */
    DATABASE("database", '\uf1c0'),
    /** file-pdf-o icon. */
    FILE_PDF_O("file-pdf-o", '\uf1c1'),
    /** file-word-o icon. */
    FILE_WORD_O("file-word-o", '\uf1c2'),
    /** file-excel-o icon. */
    FILE_EXCEL_O("file-excel-o", '\uf1c3'),
    /** file-powerpoint-o icon. */
    FILE_POWERPOINT_O("file-powerpoint-o", '\uf1c4'),
    /** file-photo-o icon. */
    FILE_PHOTO_O("file-photo-o", '\uf1c5'),
    /** file-picture-o icon. */
    FILE_PICTURE_O("file-picture-o", '\uf1c5'),
    /** file-image-o icon. */
    FILE_IMAGE_O("file-image-o", '\uf1c5'),
    /** file-zip-o icon. */
    FILE_ZIP_O("file-zip-o", '\uf1c6'),
    /** file-archive-o icon. */
    FILE_ARCHIVE_O("file-archive-o", '\uf1c6'),
    /** file-sound-o icon. */
    FILE_SOUND_O("file-sound-o", '\uf1c7'),
    /** file-audio-o icon. */
    FILE_AUDIO_O("file-audio-o", '\uf1c7'),
    /** file-movie-o icon. */
    FILE_MOVIE_O("file-movie-o", '\uf1c8'),
    /** file-video-o icon. */
    FILE_VIDEO_O("file-video-o", '\uf1c8'),
    /** file-code-o icon. */
    FILE_CODE_O("file-code-o", '\uf1c9'),
    /** vine icon. */
    VINE("vine", '\uf1ca'),
    /** codepen icon. */
    CODEPEN("codepen", '\uf1cb'),
    /** jsfiddle icon. */
    JSFIDDLE("jsfiddle", '\uf1cc'),
    /** life-bouy icon. */
    LIFE_BOUY("life-bouy", '\uf1cd'),
    /** life-buoy icon. */
    LIFE_BUOY("life-buoy", '\uf1cd'),
    /** life-saver icon. */
    LIFE_SAVER("life-saver", '\uf1cd'),
    /** support icon. */
    SUPPORT("support", '\uf1cd'),
    /** life-ring icon. */
    LIFE_RING("life-ring", '\uf1cd'),
    /** circle-o-notch icon. */
    CIRCLE_O_NOTCH("circle-o-notch", '\uf1ce'),
    /** ra icon. */
    RA("ra", '\uf1d0'),
    /** rebel icon. */
    REBEL("rebel", '\uf1d0'),
    /** ge icon. */
    GE("ge", '\uf1d1'),
    /** empire icon. */
    EMPIRE("empire", '\uf1d1'),
    /** git-square icon. */
    GIT_SQUARE("git-square", '\uf1d2'),
    /** git icon. */
    GIT("git", '\uf1d3'),
    /** y-combinator-square icon. */
    Y_COMBINATOR_SQUARE("y-combinator-square", '\uf1d4'),
    /** yc-square icon. */
    YC_SQUARE("yc-square", '\uf1d4'),
    /** hacker-news icon. */
    HACKER_NEWS("hacker-news", '\uf1d4'),
    /** tencent-weibo icon. */
    TENCENT_WEIBO("tencent-weibo", '\uf1d5'),
    /** qq icon. */
    QQ("qq", '\uf1d6'),
    /** wechat icon. */
    WECHAT("wechat", '\uf1d7'),
    /** weixin icon. */
    WEIXIN("weixin", '\uf1d7'),
    /** send icon. */
    SEND("send", '\uf1d8'),
    /** paper-plane icon. */
    PAPER_PLANE("paper-plane", '\uf1d8'),
    /** send-o icon. */
    SEND_O("send-o", '\uf1d9'),
    /** paper-plane-o icon. */
    PAPER_PLANE_O("paper-plane-o", '\uf1d9'),
    /** history icon. */
    HISTORY("history", '\uf1da'),
    /** circle-thin icon. */
    CIRCLE_THIN("circle-thin", '\uf1db'),
    /** header icon. */
    HEADER("header", '\uf1dc'),
    /** paragraph icon. */
    PARAGRAPH("paragraph", '\uf1dd'),
    /** sliders icon. */
    SLIDERS("sliders", '\uf1de'),
    /** share-alt icon. */
    SHARE_ALT("share-alt", '\uf1e0'),
    /** share-alt-square icon. */
    SHARE_ALT_SQUARE("share-alt-square", '\uf1e1'),
    /** bomb icon. */
    BOMB("bomb", '\uf1e2'),
    /** soccer-ball-o icon. */
    SOCCER_BALL_O("soccer-ball-o", '\uf1e3'),
    /** futbol-o icon. */
    FUTBOL_O("futbol-o", '\uf1e3'),
    /** tty icon. */
    TTY("tty", '\uf1e4'),
    /** binoculars icon. */
    BINOCULARS("binoculars", '\uf1e5'),
    /** plug icon. */
    PLUG("plug", '\uf1e6'),
    /** slideshare icon. */
    SLIDESHARE("slideshare", '\uf1e7'),
    /** twitch icon. */
    TWITCH("twitch", '\uf1e8'),
    /** yelp icon. */
    YELP("yelp", '\uf1e9'),
    /** newspaper-o icon. */
    NEWSPAPER_O("newspaper-o", '\uf1ea'),
    /** wifi icon. */
    WIFI("wifi", '\uf1eb'),
    /** calculator icon. */
    CALCULATOR("calculator", '\uf1ec'),
    /** paypal icon. */
    PAYPAL("paypal", '\uf1ed'),
    /** google-wallet icon. */
    GOOGLE_WALLET("google-wallet", '\uf1ee'),
    /** cc-visa icon. */
    CC_VISA("cc-visa", '\uf1f0'),
    /** cc-mastercard icon. */
    CC_MASTERCARD("cc-mastercard", '\uf1f1'),
    /** cc-discover icon. */
    CC_DISCOVER("cc-discover", '\uf1f2'),
    /** cc-amex icon. */
    CC_AMEX("cc-amex", '\uf1f3'),
    /** cc-paypal icon. */
    CC_PAYPAL("cc-paypal", '\uf1f4'),
    /** cc-stripe icon. */
    CC_STRIPE("cc-stripe", '\uf1f5'),
    /** bell-slash icon. */
    BELL_SLASH("bell-slash", '\uf1f6'),
    /** bell-slash-o icon. */
    BELL_SLASH_O("bell-slash-o", '\uf1f7'),
    /** trash icon. */
    TRASH("trash", '\uf1f8'),
    /** copyright icon. */
    COPYRIGHT("copyright", '\uf1f9'),
    /** at icon. */
    AT("at", '\uf1fa'),
    /** eyedropper icon. */
    EYEDROPPER("eyedropper", '\uf1fb'),
    /** paint-brush icon. */
    PAINT_BRUSH("paint-brush", '\uf1fc'),
    /** birthday-cake icon. */
    BIRTHDAY_CAKE("birthday-cake", '\uf1fd'),
    /** area-chart icon. */
    AREA_CHART("area-chart", '\uf1fe'),
    /** pie-chart icon. */
    PIE_CHART("pie-chart", '\uf200'),
    /** line-chart icon. */
    LINE_CHART("line-chart", '\uf201'),
    /** lastfm icon. */
    LASTFM("lastfm", '\uf202'),
    /** lastfm-square icon. */
    LASTFM_SQUARE("lastfm-square", '\uf203'),
    /** toggle-off icon. */
    TOGGLE_OFF("toggle-off", '\uf204'),
    /** toggle-on icon. */
    TOGGLE_ON("toggle-on", '\uf205'),
    /** bicycle icon. */
    BICYCLE("bicycle", '\uf206'),
    /** bus icon. */
    BUS("bus", '\uf207'),
    /** ioxhost icon. */
    IOXHOST("ioxhost", '\uf208'),
    /** angellist icon. */
    ANGELLIST("angellist", '\uf209'),
    /** cc icon. */
    CC("cc", '\uf20a'),
    /** shekel icon. */
    SHEKEL("shekel", '\uf20b'),
    /** sheqel icon. */
    SHEQEL("sheqel", '\uf20b'),
    /** ils icon. */
    ILS("ils", '\uf20b'),
    /** meanpath icon. */
    MEANPATH("meanpath", '\uf20c'),
    /** buysellads icon. */
    BUYSELLADS("buysellads", '\uf20d'),
    /** connectdevelop icon. */
    CONNECTDEVELOP("connectdevelop", '\uf20e'),
    /** dashcube icon. */
    DASHCUBE("dashcube", '\uf210'),
    /** forumbee icon. */
    FORUMBEE("forumbee", '\uf211'),
    /** leanpub icon. */
    LEANPUB("leanpub", '\uf212'),
    /** sellsy icon. */
    SELLSY("sellsy", '\uf213'),
    /** shirtsinbulk icon. */
    SHIRTSINBULK("shirtsinbulk", '\uf214'),
    /** simplybuilt icon. */
    SIMPLYBUILT("simplybuilt", '\uf215'),
    /** skyatlas icon. */
    SKYATLAS("skyatlas", '\uf216'),
    /** cart-plus icon. */
    CART_PLUS("cart-plus", '\uf217'),
    /** cart-arrow-down icon. */
    CART_ARROW_DOWN("cart-arrow-down", '\uf218'),
    /** diamond icon. */
    DIAMOND("diamond", '\uf219'),
    /** ship icon. */
    SHIP("ship", '\uf21a'),
    /** user-secret icon. */
    USER_SECRET("user-secret", '\uf21b'),
    /** motorcycle icon. */
    MOTORCYCLE("motorcycle", '\uf21c'),
    /** street-view icon. */
    STREET_VIEW("street-view", '\uf21d'),
    /** heartbeat icon. */
    HEARTBEAT("heartbeat", '\uf21e'),
    /** venus icon. */
    VENUS("venus", '\uf221'),
    /** mars icon. */
    MARS("mars", '\uf222'),
    /** mercury icon. */
    MERCURY("mercury", '\uf223'),
    /** intersex icon. */
    INTERSEX("intersex", '\uf224'),
    /** transgender icon. */
    TRANSGENDER("transgender", '\uf224'),
    /** transgender-alt icon. */
    TRANSGENDER_ALT("transgender-alt", '\uf225'),
    /** venus-double icon. */
    VENUS_DOUBLE("venus-double", '\uf226'),
    /** mars-double icon. */
    MARS_DOUBLE("mars-double", '\uf227'),
    /** venus-mars icon. */
    VENUS_MARS("venus-mars", '\uf228'),
    /** mars-stroke icon. */
    MARS_STROKE("mars-stroke", '\uf229'),
    /** mars-stroke-v icon. */
    MARS_STROKE_V("mars-stroke-v", '\uf22a'),
    /** mars-stroke-h icon. */
    MARS_STROKE_H("mars-stroke-h", '\uf22b'),
    /** neuter icon. */
    NEUTER("neuter", '\uf22c'),
    /** genderless icon. */
    GENDERLESS("genderless", '\uf22d'),
    /** facebook-official icon. */
    FACEBOOK_OFFICIAL("facebook-official", '\uf230'),
    /** pinterest-p icon. */
    PINTEREST_P("pinterest-p", '\uf231'),
    /** whatsapp icon. */
    WHATSAPP("whatsapp", '\uf232'),
    /** server icon. */
    SERVER("server", '\uf233'),
    /** user-plus icon. */
    USER_PLUS("user-plus", '\uf234'),
    /** user-times icon. */
    USER_TIMES("user-times", '\uf235'),
    /** hotel icon. */
    HOTEL("hotel", '\uf236'),
    /** bed icon. */
    BED("bed", '\uf236'),
    /** viacoin icon. */
    VIACOIN("viacoin", '\uf237'),
    /** train icon. */
    TRAIN("train", '\uf238'),
    /** subway icon. */
    SUBWAY("subway", '\uf239'),
    /** medium icon. */
    MEDIUM("medium", '\uf23a'),
    /** yc icon. */
    YC("yc", '\uf23b'),
    /** y-combinator icon. */
    Y_COMBINATOR("y-combinator", '\uf23b'),
    /** optin-monster icon. */
    OPTIN_MONSTER("optin-monster", '\uf23c'),
    /** opencart icon. */
    OPENCART("opencart", '\uf23d'),
    /** expeditedssl icon. */
    EXPEDITEDSSL("expeditedssl", '\uf23e'),
    /** battery-4 icon. */
    BATTERY_4("battery-4", '\uf240'),
    /** battery-full icon. */
    BATTERY_FULL("battery-full", '\uf240'),
    /** battery-3 icon. */
    BATTERY_3("battery-3", '\uf241'),
    /** battery-three-quarters icon. */
    BATTERY_THREE_QUARTERS("battery-three-quarters", '\uf241'),
    /** battery-2 icon. */
    BATTERY_2("battery-2", '\uf242'),
    /** battery-half icon. */
    BATTERY_HALF("battery-half", '\uf242'),
    /** battery-1 icon. */
    BATTERY_1("battery-1", '\uf243'),
    /** battery-quarter icon. */
    BATTERY_QUARTER("battery-quarter", '\uf243'),
    /** battery-0 icon. */
    BATTERY_0("battery-0", '\uf244'),
    /** battery-empty icon. */
    BATTERY_EMPTY("battery-empty", '\uf244'),
    /** mouse-pointer icon. */
    MOUSE_POINTER("mouse-pointer", '\uf245'),
    /** i-cursor icon. */
    I_CURSOR("i-cursor", '\uf246'),
    /** object-group icon. */
    OBJECT_GROUP("object-group", '\uf247'),
    /** object-ungroup icon. */
    OBJECT_UNGROUP("object-ungroup", '\uf248'),
    /** sticky-note icon. */
    STICKY_NOTE("sticky-note", '\uf249'),
    /** sticky-note-o icon. */
    STICKY_NOTE_O("sticky-note-o", '\uf24a'),
    /** cc-jcb icon. */
    CC_JCB("cc-jcb", '\uf24b'),
    /** cc-diners-club icon. */
    CC_DINERS_CLUB("cc-diners-club", '\uf24c'),
    /** clone icon. */
    CLONE("clone", '\uf24d'),
    /** balance-scale icon. */
    BALANCE_SCALE("balance-scale", '\uf24e'),
    /** hourglass-o icon. */
    HOURGLASS_O("hourglass-o", '\uf250'),
    /** hourglass-1 icon. */
    HOURGLASS_1("hourglass-1", '\uf251'),
    /** hourglass-start icon. */
    HOURGLASS_START("hourglass-start", '\uf251'),
    /** hourglass-2 icon. */
    HOURGLASS_2("hourglass-2", '\uf252'),
    /** hourglass-half icon. */
    HOURGLASS_HALF("hourglass-half", '\uf252'),
    /** hourglass-3 icon. */
    HOURGLASS_3("hourglass-3", '\uf253'),
    /** hourglass-end icon. */
    HOURGLASS_END("hourglass-end", '\uf253'),
    /** hourglass icon. */
    HOURGLASS("hourglass", '\uf254'),
    /** hand-grab-o icon. */
    HAND_GRAB_O("hand-grab-o", '\uf255'),
    /** hand-rock-o icon. */
    HAND_ROCK_O("hand-rock-o", '\uf255'),
    /** hand-stop-o icon. */
    HAND_STOP_O("hand-stop-o", '\uf256'),
    /** hand-paper-o icon. */
    HAND_PAPER_O("hand-paper-o", '\uf256'),
    /** hand-scissors-o icon. */
    HAND_SCISSORS_O("hand-scissors-o", '\uf257'),
    /** hand-lizard-o icon. */
    HAND_LIZARD_O("hand-lizard-o", '\uf258'),
    /** hand-spock-o icon. */
    HAND_SPOCK_O("hand-spock-o", '\uf259'),
    /** hand-pointer-o icon. */
    HAND_POINTER_O("hand-pointer-o", '\uf25a'),
    /** hand-peace-o icon. */
    HAND_PEACE_O("hand-peace-o", '\uf25b'),
    /** trademark icon. */
    TRADEMARK("trademark", '\uf25c'),
    /** registered icon. */
    REGISTERED("registered", '\uf25d'),
    /** creative-commons icon. */
    CREATIVE_COMMONS("creative-commons", '\uf25e'),
    /** gg icon. */
    GG("gg", '\uf260'),
    /** gg-circle icon. */
    GG_CIRCLE("gg-circle", '\uf261'),
    /** tripadvisor icon. */
    TRIPADVISOR("tripadvisor", '\uf262'),
    /** odnoklassniki icon. */
    ODNOKLASSNIKI("odnoklassniki", '\uf263'),
    /** odnoklassniki-square icon. */
    ODNOKLASSNIKI_SQUARE("odnoklassniki-square", '\uf264'),
    /** get-pocket icon. */
    GET_POCKET("get-pocket", '\uf265'),
    /** wikipedia-w icon. */
    WIKIPEDIA_W("wikipedia-w", '\uf266'),
    /** safari icon. */
    SAFARI("safari", '\uf267'),
    /** chrome icon. */
    CHROME("chrome", '\uf268'),
    /** firefox icon. */
    FIREFOX("firefox", '\uf269'),
    /** opera icon. */
    OPERA("opera", '\uf26a'),
    /** internet-explorer icon. */
    INTERNET_EXPLORER("internet-explorer", '\uf26b'),
    /** tv icon. */
    TV("tv", '\uf26c'),
    /** television icon. */
    TELEVISION("television", '\uf26c'),
    /** contao icon. */
    CONTAO("contao", '\uf26d'),
    /** 500px icon. */
    FIVEHUNDRED_PX("500px", '\uf26e'),
    /** amazon icon. */
    AMAZON("amazon", '\uf270'),
    /** calendar-plus-o icon. */
    CALENDAR_PLUS_O("calendar-plus-o", '\uf271'),
    /** calendar-minus-o icon. */
    CALENDAR_MINUS_O("calendar-minus-o", '\uf272'),
    /** calendar-times-o icon. */
    CALENDAR_TIMES_O("calendar-times-o", '\uf273'),
    /** calendar-check-o icon. */
    CALENDAR_CHECK_O("calendar-check-o", '\uf274'),
    /** industry icon. */
    INDUSTRY("industry", '\uf275'),
    /** map-pin icon. */
    MAP_PIN("map-pin", '\uf276'),
    /** map-signs icon. */
    MAP_SIGNS("map-signs", '\uf277'),
    /** map-o icon. */
    MAP_O("map-o", '\uf278'),
    /** map icon. */
    MAP("map", '\uf279'),
    /** commenting icon. */
    COMMENTING("commenting", '\uf27a'),
    /** commenting-o icon. */
    COMMENTING_O("commenting-o", '\uf27b'),
    /** houzz icon. */
    HOUZZ("houzz", '\uf27c'),
    /** vimeo icon. */
    VIMEO("vimeo", '\uf27d'),
    /** black-tie icon. */
    BLACK_TIE("black-tie", '\uf27e'),
    /** fonticons icon. */
    FONTICONS("fonticons", '\uf280');

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/** Cache for faster resolve. */
	private static final HashMap<String, FontAwesome> CACHE = new HashMap<String, FontAwesome>();
	
    /** the icon name. */
    private String name;
    /** the icon code. */
    private char code;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    static
    {
    	for (FontAwesome font : values())
    	{
    		CACHE.put(font.name, font);
    	}
    }
    
    /**
     * Creates a new instance of <code>FontAwesome</code>.
     * 
     * @param pName the icon name
     * @param pCode the icon character
     */
    FontAwesome(String pName, char pCode) 
    {
        this.name = pName;
        this.code = pCode;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Tries to find an instance of <code>FontAwesome</code> by name.
     * 
     * @param pName the name
     * @return the found instance
     * @throws IllegalArgumentException if name wasn't found or given <code>pName</code> was <code>null</code>
     */
    public static FontAwesome resolve(String pName) 
    {
    	FontAwesome font = CACHE.get(pName);
    	
    	if (font == null)
    	{
    		font = CACHE.get(pName.replace("_", "-").toLowerCase());
    	}
    	
    	if (font == null)
    	{
    		throw new IllegalArgumentException("Icon " + pName + " was not found!");
    	}
    	
    	return font;
    }

    /**
     * Gets the icon name.
     * 
     * @return the name
     */
    public String getName() 
    {
        return name;
    }

    /**
     * Gets the icon character.
     * 
     * @return the character
     */
    public char getCode() 
    {
        return code;
    }
    
}   // FontAwesome
