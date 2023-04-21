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
package javax.rad.genui;

/**
 * The <code>IFontAwesome</code> interface predefines supported FontAwesome icons.
 * 
 * @author René Jahn
 */
public interface IFontAwesome
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the name of the small(default: 16x16 px) glass image (used for menuitems or buttons). */
    public static final String GLASS_SMALL = "FontAwesome.glass";
    /** the name of the large(default: 24x24 px) glass image (used for toolbar buttons or icons). */
    public static final String GLASS_LARGE = "FontAwesome.glass;size=24";

    /** the name of the small(default: 16x16 px) music image (used for menuitems or buttons). */
    public static final String MUSIC_SMALL = "FontAwesome.music";
    /** the name of the large(default: 24x24 px) music image (used for toolbar buttons or icons). */
    public static final String MUSIC_LARGE = "FontAwesome.music;size=24";

    /** the name of the small(default: 16x16 px) search image (used for menuitems or buttons). */
    public static final String SEARCH_SMALL = "FontAwesome.search";
    /** the name of the large(default: 24x24 px) search image (used for toolbar buttons or icons). */
    public static final String SEARCH_LARGE = "FontAwesome.search;size=24";

    /** the name of the small(default: 16x16 px) envelope-o image (used for menuitems or buttons). */
    public static final String ENVELOPE_O_SMALL = "FontAwesome.envelope-o";
    /** the name of the large(default: 24x24 px) envelope-o image (used for toolbar buttons or icons). */
    public static final String ENVELOPE_O_LARGE = "FontAwesome.envelope-o;size=24";

    /** the name of the small(default: 16x16 px) heart image (used for menuitems or buttons). */
    public static final String HEART_SMALL = "FontAwesome.heart";
    /** the name of the large(default: 24x24 px) heart image (used for toolbar buttons or icons). */
    public static final String HEART_LARGE = "FontAwesome.heart;size=24";

    /** the name of the small(default: 16x16 px) star image (used for menuitems or buttons). */
    public static final String STAR_SMALL = "FontAwesome.star";
    /** the name of the large(default: 24x24 px) star image (used for toolbar buttons or icons). */
    public static final String STAR_LARGE = "FontAwesome.star;size=24";

    /** the name of the small(default: 16x16 px) star-o image (used for menuitems or buttons). */
    public static final String STAR_O_SMALL = "FontAwesome.star-o";
    /** the name of the large(default: 24x24 px) star-o image (used for toolbar buttons or icons). */
    public static final String STAR_O_LARGE = "FontAwesome.star-o;size=24";

    /** the name of the small(default: 16x16 px) user image (used for menuitems or buttons). */
    public static final String USER_SMALL = "FontAwesome.user";
    /** the name of the large(default: 24x24 px) user image (used for toolbar buttons or icons). */
    public static final String USER_LARGE = "FontAwesome.user;size=24";

    /** the name of the small(default: 16x16 px) film image (used for menuitems or buttons). */
    public static final String FILM_SMALL = "FontAwesome.film";
    /** the name of the large(default: 24x24 px) film image (used for toolbar buttons or icons). */
    public static final String FILM_LARGE = "FontAwesome.film;size=24";

    /** the name of the small(default: 16x16 px) th-large image (used for menuitems or buttons). */
    public static final String TH_LARGE_SMALL = "FontAwesome.th-large";
    /** the name of the large(default: 24x24 px) th-large image (used for toolbar buttons or icons). */
    public static final String TH_LARGE_LARGE = "FontAwesome.th-large;size=24";

    /** the name of the small(default: 16x16 px) th image (used for menuitems or buttons). */
    public static final String TH_SMALL = "FontAwesome.th";
    /** the name of the large(default: 24x24 px) th image (used for toolbar buttons or icons). */
    public static final String TH_LARGE = "FontAwesome.th;size=24";

    /** the name of the small(default: 16x16 px) th-list image (used for menuitems or buttons). */
    public static final String TH_LIST_SMALL = "FontAwesome.th-list";
    /** the name of the large(default: 24x24 px) th-list image (used for toolbar buttons or icons). */
    public static final String TH_LIST_LARGE = "FontAwesome.th-list;size=24";

    /** the name of the small(default: 16x16 px) check image (used for menuitems or buttons). */
    public static final String CHECK_SMALL = "FontAwesome.check";
    /** the name of the large(default: 24x24 px) check image (used for toolbar buttons or icons). */
    public static final String CHECK_LARGE = "FontAwesome.check;size=24";

    /** the name of the small(default: 16x16 px) remove image (used for menuitems or buttons). */
    public static final String REMOVE_SMALL = "FontAwesome.remove";
    /** the name of the large(default: 24x24 px) remove image (used for toolbar buttons or icons). */
    public static final String REMOVE_LARGE = "FontAwesome.remove;size=24";

    /** the name of the small(default: 16x16 px) close image (used for menuitems or buttons). */
    public static final String CLOSE_SMALL = "FontAwesome.close";
    /** the name of the large(default: 24x24 px) close image (used for toolbar buttons or icons). */
    public static final String CLOSE_LARGE = "FontAwesome.close;size=24";

    /** the name of the small(default: 16x16 px) times image (used for menuitems or buttons). */
    public static final String TIMES_SMALL = "FontAwesome.times";
    /** the name of the large(default: 24x24 px) times image (used for toolbar buttons or icons). */
    public static final String TIMES_LARGE = "FontAwesome.times;size=24";

    /** the name of the small(default: 16x16 px) search-plus image (used for menuitems or buttons). */
    public static final String SEARCH_PLUS_SMALL = "FontAwesome.search-plus";
    /** the name of the large(default: 24x24 px) search-plus image (used for toolbar buttons or icons). */
    public static final String SEARCH_PLUS_LARGE = "FontAwesome.search-plus;size=24";

    /** the name of the small(default: 16x16 px) search-minus image (used for menuitems or buttons). */
    public static final String SEARCH_MINUS_SMALL = "FontAwesome.search-minus";
    /** the name of the large(default: 24x24 px) search-minus image (used for toolbar buttons or icons). */
    public static final String SEARCH_MINUS_LARGE = "FontAwesome.search-minus;size=24";

    /** the name of the small(default: 16x16 px) power-off image (used for menuitems or buttons). */
    public static final String POWER_OFF_SMALL = "FontAwesome.power-off";
    /** the name of the large(default: 24x24 px) power-off image (used for toolbar buttons or icons). */
    public static final String POWER_OFF_LARGE = "FontAwesome.power-off;size=24";

    /** the name of the small(default: 16x16 px) signal image (used for menuitems or buttons). */
    public static final String SIGNAL_SMALL = "FontAwesome.signal";
    /** the name of the large(default: 24x24 px) signal image (used for toolbar buttons or icons). */
    public static final String SIGNAL_LARGE = "FontAwesome.signal;size=24";

    /** the name of the small(default: 16x16 px) gear image (used for menuitems or buttons). */
    public static final String GEAR_SMALL = "FontAwesome.gear";
    /** the name of the large(default: 24x24 px) gear image (used for toolbar buttons or icons). */
    public static final String GEAR_LARGE = "FontAwesome.gear;size=24";

    /** the name of the small(default: 16x16 px) cog image (used for menuitems or buttons). */
    public static final String COG_SMALL = "FontAwesome.cog";
    /** the name of the large(default: 24x24 px) cog image (used for toolbar buttons or icons). */
    public static final String COG_LARGE = "FontAwesome.cog;size=24";

    /** the name of the small(default: 16x16 px) trash-o image (used for menuitems or buttons). */
    public static final String TRASH_O_SMALL = "FontAwesome.trash-o";
    /** the name of the large(default: 24x24 px) trash-o image (used for toolbar buttons or icons). */
    public static final String TRASH_O_LARGE = "FontAwesome.trash-o;size=24";

    /** the name of the small(default: 16x16 px) home image (used for menuitems or buttons). */
    public static final String HOME_SMALL = "FontAwesome.home";
    /** the name of the large(default: 24x24 px) home image (used for toolbar buttons or icons). */
    public static final String HOME_LARGE = "FontAwesome.home;size=24";

    /** the name of the small(default: 16x16 px) file-o image (used for menuitems or buttons). */
    public static final String FILE_O_SMALL = "FontAwesome.file-o";
    /** the name of the large(default: 24x24 px) file-o image (used for toolbar buttons or icons). */
    public static final String FILE_O_LARGE = "FontAwesome.file-o;size=24";

    /** the name of the small(default: 16x16 px) clock-o image (used for menuitems or buttons). */
    public static final String CLOCK_O_SMALL = "FontAwesome.clock-o";
    /** the name of the large(default: 24x24 px) clock-o image (used for toolbar buttons or icons). */
    public static final String CLOCK_O_LARGE = "FontAwesome.clock-o;size=24";

    /** the name of the small(default: 16x16 px) road image (used for menuitems or buttons). */
    public static final String ROAD_SMALL = "FontAwesome.road";
    /** the name of the large(default: 24x24 px) road image (used for toolbar buttons or icons). */
    public static final String ROAD_LARGE = "FontAwesome.road;size=24";

    /** the name of the small(default: 16x16 px) download image (used for menuitems or buttons). */
    public static final String DOWNLOAD_SMALL = "FontAwesome.download";
    /** the name of the large(default: 24x24 px) download image (used for toolbar buttons or icons). */
    public static final String DOWNLOAD_LARGE = "FontAwesome.download;size=24";

    /** the name of the small(default: 16x16 px) arrow-circle-o-down image (used for menuitems or buttons). */
    public static final String ARROW_CIRCLE_O_DOWN_SMALL = "FontAwesome.arrow-circle-o-down";
    /** the name of the large(default: 24x24 px) arrow-circle-o-down image (used for toolbar buttons or icons). */
    public static final String ARROW_CIRCLE_O_DOWN_LARGE = "FontAwesome.arrow-circle-o-down;size=24";

    /** the name of the small(default: 16x16 px) arrow-circle-o-up image (used for menuitems or buttons). */
    public static final String ARROW_CIRCLE_O_UP_SMALL = "FontAwesome.arrow-circle-o-up";
    /** the name of the large(default: 24x24 px) arrow-circle-o-up image (used for toolbar buttons or icons). */
    public static final String ARROW_CIRCLE_O_UP_LARGE = "FontAwesome.arrow-circle-o-up;size=24";

    /** the name of the small(default: 16x16 px) inbox image (used for menuitems or buttons). */
    public static final String INBOX_SMALL = "FontAwesome.inbox";
    /** the name of the large(default: 24x24 px) inbox image (used for toolbar buttons or icons). */
    public static final String INBOX_LARGE = "FontAwesome.inbox;size=24";

    /** the name of the small(default: 16x16 px) play-circle-o image (used for menuitems or buttons). */
    public static final String PLAY_CIRCLE_O_SMALL = "FontAwesome.play-circle-o";
    /** the name of the large(default: 24x24 px) play-circle-o image (used for toolbar buttons or icons). */
    public static final String PLAY_CIRCLE_O_LARGE = "FontAwesome.play-circle-o;size=24";

    /** the name of the small(default: 16x16 px) rotate-right image (used for menuitems or buttons). */
    public static final String ROTATE_RIGHT_SMALL = "FontAwesome.rotate-right";
    /** the name of the large(default: 24x24 px) rotate-right image (used for toolbar buttons or icons). */
    public static final String ROTATE_RIGHT_LARGE = "FontAwesome.rotate-right;size=24";

    /** the name of the small(default: 16x16 px) repeat image (used for menuitems or buttons). */
    public static final String REPEAT_SMALL = "FontAwesome.repeat";
    /** the name of the large(default: 24x24 px) repeat image (used for toolbar buttons or icons). */
    public static final String REPEAT_LARGE = "FontAwesome.repeat;size=24";

    /** the name of the small(default: 16x16 px) refresh image (used for menuitems or buttons). */
    public static final String REFRESH_SMALL = "FontAwesome.refresh";
    /** the name of the large(default: 24x24 px) refresh image (used for toolbar buttons or icons). */
    public static final String REFRESH_LARGE = "FontAwesome.refresh;size=24";

    /** the name of the small(default: 16x16 px) list-alt image (used for menuitems or buttons). */
    public static final String LIST_ALT_SMALL = "FontAwesome.list-alt";
    /** the name of the large(default: 24x24 px) list-alt image (used for toolbar buttons or icons). */
    public static final String LIST_ALT_LARGE = "FontAwesome.list-alt;size=24";

    /** the name of the small(default: 16x16 px) lock image (used for menuitems or buttons). */
    public static final String LOCK_SMALL = "FontAwesome.lock";
    /** the name of the large(default: 24x24 px) lock image (used for toolbar buttons or icons). */
    public static final String LOCK_LARGE = "FontAwesome.lock;size=24";

    /** the name of the small(default: 16x16 px) flag image (used for menuitems or buttons). */
    public static final String FLAG_SMALL = "FontAwesome.flag";
    /** the name of the large(default: 24x24 px) flag image (used for toolbar buttons or icons). */
    public static final String FLAG_LARGE = "FontAwesome.flag;size=24";

    /** the name of the small(default: 16x16 px) headphones image (used for menuitems or buttons). */
    public static final String HEADPHONES_SMALL = "FontAwesome.headphones";
    /** the name of the large(default: 24x24 px) headphones image (used for toolbar buttons or icons). */
    public static final String HEADPHONES_LARGE = "FontAwesome.headphones;size=24";

    /** the name of the small(default: 16x16 px) volume-off image (used for menuitems or buttons). */
    public static final String VOLUME_OFF_SMALL = "FontAwesome.volume-off";
    /** the name of the large(default: 24x24 px) volume-off image (used for toolbar buttons or icons). */
    public static final String VOLUME_OFF_LARGE = "FontAwesome.volume-off;size=24";

    /** the name of the small(default: 16x16 px) volume-down image (used for menuitems or buttons). */
    public static final String VOLUME_DOWN_SMALL = "FontAwesome.volume-down";
    /** the name of the large(default: 24x24 px) volume-down image (used for toolbar buttons or icons). */
    public static final String VOLUME_DOWN_LARGE = "FontAwesome.volume-down;size=24";

    /** the name of the small(default: 16x16 px) volume-up image (used for menuitems or buttons). */
    public static final String VOLUME_UP_SMALL = "FontAwesome.volume-up";
    /** the name of the large(default: 24x24 px) volume-up image (used for toolbar buttons or icons). */
    public static final String VOLUME_UP_LARGE = "FontAwesome.volume-up;size=24";

    /** the name of the small(default: 16x16 px) qrcode image (used for menuitems or buttons). */
    public static final String QRCODE_SMALL = "FontAwesome.qrcode";
    /** the name of the large(default: 24x24 px) qrcode image (used for toolbar buttons or icons). */
    public static final String QRCODE_LARGE = "FontAwesome.qrcode;size=24";

    /** the name of the small(default: 16x16 px) barcode image (used for menuitems or buttons). */
    public static final String BARCODE_SMALL = "FontAwesome.barcode";
    /** the name of the large(default: 24x24 px) barcode image (used for toolbar buttons or icons). */
    public static final String BARCODE_LARGE = "FontAwesome.barcode;size=24";

    /** the name of the small(default: 16x16 px) tag image (used for menuitems or buttons). */
    public static final String TAG_SMALL = "FontAwesome.tag";
    /** the name of the large(default: 24x24 px) tag image (used for toolbar buttons or icons). */
    public static final String TAG_LARGE = "FontAwesome.tag;size=24";

    /** the name of the small(default: 16x16 px) tags image (used for menuitems or buttons). */
    public static final String TAGS_SMALL = "FontAwesome.tags";
    /** the name of the large(default: 24x24 px) tags image (used for toolbar buttons or icons). */
    public static final String TAGS_LARGE = "FontAwesome.tags;size=24";

    /** the name of the small(default: 16x16 px) book image (used for menuitems or buttons). */
    public static final String BOOK_SMALL = "FontAwesome.book";
    /** the name of the large(default: 24x24 px) book image (used for toolbar buttons or icons). */
    public static final String BOOK_LARGE = "FontAwesome.book;size=24";

    /** the name of the small(default: 16x16 px) bookmark image (used for menuitems or buttons). */
    public static final String BOOKMARK_SMALL = "FontAwesome.bookmark";
    /** the name of the large(default: 24x24 px) bookmark image (used for toolbar buttons or icons). */
    public static final String BOOKMARK_LARGE = "FontAwesome.bookmark;size=24";

    /** the name of the small(default: 16x16 px) print image (used for menuitems or buttons). */
    public static final String PRINT_SMALL = "FontAwesome.print";
    /** the name of the large(default: 24x24 px) print image (used for toolbar buttons or icons). */
    public static final String PRINT_LARGE = "FontAwesome.print;size=24";

    /** the name of the small(default: 16x16 px) camera image (used for menuitems or buttons). */
    public static final String CAMERA_SMALL = "FontAwesome.camera";
    /** the name of the large(default: 24x24 px) camera image (used for toolbar buttons or icons). */
    public static final String CAMERA_LARGE = "FontAwesome.camera;size=24";

    /** the name of the small(default: 16x16 px) font image (used for menuitems or buttons). */
    public static final String FONT_SMALL = "FontAwesome.font";
    /** the name of the large(default: 24x24 px) font image (used for toolbar buttons or icons). */
    public static final String FONT_LARGE = "FontAwesome.font;size=24";

    /** the name of the small(default: 16x16 px) bold image (used for menuitems or buttons). */
    public static final String BOLD_SMALL = "FontAwesome.bold";
    /** the name of the large(default: 24x24 px) bold image (used for toolbar buttons or icons). */
    public static final String BOLD_LARGE = "FontAwesome.bold;size=24";

    /** the name of the small(default: 16x16 px) italic image (used for menuitems or buttons). */
    public static final String ITALIC_SMALL = "FontAwesome.italic";
    /** the name of the large(default: 24x24 px) italic image (used for toolbar buttons or icons). */
    public static final String ITALIC_LARGE = "FontAwesome.italic;size=24";

    /** the name of the small(default: 16x16 px) text-height image (used for menuitems or buttons). */
    public static final String TEXT_HEIGHT_SMALL = "FontAwesome.text-height";
    /** the name of the large(default: 24x24 px) text-height image (used for toolbar buttons or icons). */
    public static final String TEXT_HEIGHT_LARGE = "FontAwesome.text-height;size=24";

    /** the name of the small(default: 16x16 px) text-width image (used for menuitems or buttons). */
    public static final String TEXT_WIDTH_SMALL = "FontAwesome.text-width";
    /** the name of the large(default: 24x24 px) text-width image (used for toolbar buttons or icons). */
    public static final String TEXT_WIDTH_LARGE = "FontAwesome.text-width;size=24";

    /** the name of the small(default: 16x16 px) align-left image (used for menuitems or buttons). */
    public static final String ALIGN_LEFT_SMALL = "FontAwesome.align-left";
    /** the name of the large(default: 24x24 px) align-left image (used for toolbar buttons or icons). */
    public static final String ALIGN_LEFT_LARGE = "FontAwesome.align-left;size=24";

    /** the name of the small(default: 16x16 px) align-center image (used for menuitems or buttons). */
    public static final String ALIGN_CENTER_SMALL = "FontAwesome.align-center";
    /** the name of the large(default: 24x24 px) align-center image (used for toolbar buttons or icons). */
    public static final String ALIGN_CENTER_LARGE = "FontAwesome.align-center;size=24";

    /** the name of the small(default: 16x16 px) align-right image (used for menuitems or buttons). */
    public static final String ALIGN_RIGHT_SMALL = "FontAwesome.align-right";
    /** the name of the large(default: 24x24 px) align-right image (used for toolbar buttons or icons). */
    public static final String ALIGN_RIGHT_LARGE = "FontAwesome.align-right;size=24";

    /** the name of the small(default: 16x16 px) align-justify image (used for menuitems or buttons). */
    public static final String ALIGN_JUSTIFY_SMALL = "FontAwesome.align-justify";
    /** the name of the large(default: 24x24 px) align-justify image (used for toolbar buttons or icons). */
    public static final String ALIGN_JUSTIFY_LARGE = "FontAwesome.align-justify;size=24";

    /** the name of the small(default: 16x16 px) list image (used for menuitems or buttons). */
    public static final String LIST_SMALL = "FontAwesome.list";
    /** the name of the large(default: 24x24 px) list image (used for toolbar buttons or icons). */
    public static final String LIST_LARGE = "FontAwesome.list;size=24";

    /** the name of the small(default: 16x16 px) dedent image (used for menuitems or buttons). */
    public static final String DEDENT_SMALL = "FontAwesome.dedent";
    /** the name of the large(default: 24x24 px) dedent image (used for toolbar buttons or icons). */
    public static final String DEDENT_LARGE = "FontAwesome.dedent;size=24";

    /** the name of the small(default: 16x16 px) outdent image (used for menuitems or buttons). */
    public static final String OUTDENT_SMALL = "FontAwesome.outdent";
    /** the name of the large(default: 24x24 px) outdent image (used for toolbar buttons or icons). */
    public static final String OUTDENT_LARGE = "FontAwesome.outdent;size=24";

    /** the name of the small(default: 16x16 px) indent image (used for menuitems or buttons). */
    public static final String INDENT_SMALL = "FontAwesome.indent";
    /** the name of the large(default: 24x24 px) indent image (used for toolbar buttons or icons). */
    public static final String INDENT_LARGE = "FontAwesome.indent;size=24";

    /** the name of the small(default: 16x16 px) video-camera image (used for menuitems or buttons). */
    public static final String VIDEO_CAMERA_SMALL = "FontAwesome.video-camera";
    /** the name of the large(default: 24x24 px) video-camera image (used for toolbar buttons or icons). */
    public static final String VIDEO_CAMERA_LARGE = "FontAwesome.video-camera;size=24";

    /** the name of the small(default: 16x16 px) photo image (used for menuitems or buttons). */
    public static final String PHOTO_SMALL = "FontAwesome.photo";
    /** the name of the large(default: 24x24 px) photo image (used for toolbar buttons or icons). */
    public static final String PHOTO_LARGE = "FontAwesome.photo;size=24";

    /** the name of the small(default: 16x16 px) image image (used for menuitems or buttons). */
    public static final String IMAGE_SMALL = "FontAwesome.image";
    /** the name of the large(default: 24x24 px) image image (used for toolbar buttons or icons). */
    public static final String IMAGE_LARGE = "FontAwesome.image;size=24";

    /** the name of the small(default: 16x16 px) picture-o image (used for menuitems or buttons). */
    public static final String PICTURE_O_SMALL = "FontAwesome.picture-o";
    /** the name of the large(default: 24x24 px) picture-o image (used for toolbar buttons or icons). */
    public static final String PICTURE_O_LARGE = "FontAwesome.picture-o;size=24";

    /** the name of the small(default: 16x16 px) pencil image (used for menuitems or buttons). */
    public static final String PENCIL_SMALL = "FontAwesome.pencil";
    /** the name of the large(default: 24x24 px) pencil image (used for toolbar buttons or icons). */
    public static final String PENCIL_LARGE = "FontAwesome.pencil;size=24";

    /** the name of the small(default: 16x16 px) map-marker image (used for menuitems or buttons). */
    public static final String MAP_MARKER_SMALL = "FontAwesome.map-marker";
    /** the name of the large(default: 24x24 px) map-marker image (used for toolbar buttons or icons). */
    public static final String MAP_MARKER_LARGE = "FontAwesome.map-marker;size=24";

    /** the name of the small(default: 16x16 px) adjust image (used for menuitems or buttons). */
    public static final String ADJUST_SMALL = "FontAwesome.adjust";
    /** the name of the large(default: 24x24 px) adjust image (used for toolbar buttons or icons). */
    public static final String ADJUST_LARGE = "FontAwesome.adjust;size=24";

    /** the name of the small(default: 16x16 px) tint image (used for menuitems or buttons). */
    public static final String TINT_SMALL = "FontAwesome.tint";
    /** the name of the large(default: 24x24 px) tint image (used for toolbar buttons or icons). */
    public static final String TINT_LARGE = "FontAwesome.tint;size=24";

    /** the name of the small(default: 16x16 px) edit image (used for menuitems or buttons). */
    public static final String EDIT_SMALL = "FontAwesome.edit";
    /** the name of the large(default: 24x24 px) edit image (used for toolbar buttons or icons). */
    public static final String EDIT_LARGE = "FontAwesome.edit;size=24";

    /** the name of the small(default: 16x16 px) pencil-square-o image (used for menuitems or buttons). */
    public static final String PENCIL_SQUARE_O_SMALL = "FontAwesome.pencil-square-o";
    /** the name of the large(default: 24x24 px) pencil-square-o image (used for toolbar buttons or icons). */
    public static final String PENCIL_SQUARE_O_LARGE = "FontAwesome.pencil-square-o;size=24";

    /** the name of the small(default: 16x16 px) share-square-o image (used for menuitems or buttons). */
    public static final String SHARE_SQUARE_O_SMALL = "FontAwesome.share-square-o";
    /** the name of the large(default: 24x24 px) share-square-o image (used for toolbar buttons or icons). */
    public static final String SHARE_SQUARE_O_LARGE = "FontAwesome.share-square-o;size=24";

    /** the name of the small(default: 16x16 px) check-square-o image (used for menuitems or buttons). */
    public static final String CHECK_SQUARE_O_SMALL = "FontAwesome.check-square-o";
    /** the name of the large(default: 24x24 px) check-square-o image (used for toolbar buttons or icons). */
    public static final String CHECK_SQUARE_O_LARGE = "FontAwesome.check-square-o;size=24";

    /** the name of the small(default: 16x16 px) arrows image (used for menuitems or buttons). */
    public static final String ARROWS_SMALL = "FontAwesome.arrows";
    /** the name of the large(default: 24x24 px) arrows image (used for toolbar buttons or icons). */
    public static final String ARROWS_LARGE = "FontAwesome.arrows;size=24";

    /** the name of the small(default: 16x16 px) step-backward image (used for menuitems or buttons). */
    public static final String STEP_BACKWARD_SMALL = "FontAwesome.step-backward";
    /** the name of the large(default: 24x24 px) step-backward image (used for toolbar buttons or icons). */
    public static final String STEP_BACKWARD_LARGE = "FontAwesome.step-backward;size=24";

    /** the name of the small(default: 16x16 px) fast-backward image (used for menuitems or buttons). */
    public static final String FAST_BACKWARD_SMALL = "FontAwesome.fast-backward";
    /** the name of the large(default: 24x24 px) fast-backward image (used for toolbar buttons or icons). */
    public static final String FAST_BACKWARD_LARGE = "FontAwesome.fast-backward;size=24";

    /** the name of the small(default: 16x16 px) backward image (used for menuitems or buttons). */
    public static final String BACKWARD_SMALL = "FontAwesome.backward";
    /** the name of the large(default: 24x24 px) backward image (used for toolbar buttons or icons). */
    public static final String BACKWARD_LARGE = "FontAwesome.backward;size=24";

    /** the name of the small(default: 16x16 px) play image (used for menuitems or buttons). */
    public static final String PLAY_SMALL = "FontAwesome.play";
    /** the name of the large(default: 24x24 px) play image (used for toolbar buttons or icons). */
    public static final String PLAY_LARGE = "FontAwesome.play;size=24";

    /** the name of the small(default: 16x16 px) pause image (used for menuitems or buttons). */
    public static final String PAUSE_SMALL = "FontAwesome.pause";
    /** the name of the large(default: 24x24 px) pause image (used for toolbar buttons or icons). */
    public static final String PAUSE_LARGE = "FontAwesome.pause;size=24";

    /** the name of the small(default: 16x16 px) stop image (used for menuitems or buttons). */
    public static final String STOP_SMALL = "FontAwesome.stop";
    /** the name of the large(default: 24x24 px) stop image (used for toolbar buttons or icons). */
    public static final String STOP_LARGE = "FontAwesome.stop;size=24";

    /** the name of the small(default: 16x16 px) forward image (used for menuitems or buttons). */
    public static final String FORWARD_SMALL = "FontAwesome.forward";
    /** the name of the large(default: 24x24 px) forward image (used for toolbar buttons or icons). */
    public static final String FORWARD_LARGE = "FontAwesome.forward;size=24";

    /** the name of the small(default: 16x16 px) fast-forward image (used for menuitems or buttons). */
    public static final String FAST_FORWARD_SMALL = "FontAwesome.fast-forward";
    /** the name of the large(default: 24x24 px) fast-forward image (used for toolbar buttons or icons). */
    public static final String FAST_FORWARD_LARGE = "FontAwesome.fast-forward;size=24";

    /** the name of the small(default: 16x16 px) step-forward image (used for menuitems or buttons). */
    public static final String STEP_FORWARD_SMALL = "FontAwesome.step-forward";
    /** the name of the large(default: 24x24 px) step-forward image (used for toolbar buttons or icons). */
    public static final String STEP_FORWARD_LARGE = "FontAwesome.step-forward;size=24";

    /** the name of the small(default: 16x16 px) eject image (used for menuitems or buttons). */
    public static final String EJECT_SMALL = "FontAwesome.eject";
    /** the name of the large(default: 24x24 px) eject image (used for toolbar buttons or icons). */
    public static final String EJECT_LARGE = "FontAwesome.eject;size=24";

    /** the name of the small(default: 16x16 px) chevron-left image (used for menuitems or buttons). */
    public static final String CHEVRON_LEFT_SMALL = "FontAwesome.chevron-left";
    /** the name of the large(default: 24x24 px) chevron-left image (used for toolbar buttons or icons). */
    public static final String CHEVRON_LEFT_LARGE = "FontAwesome.chevron-left;size=24";

    /** the name of the small(default: 16x16 px) chevron-right image (used for menuitems or buttons). */
    public static final String CHEVRON_RIGHT_SMALL = "FontAwesome.chevron-right";
    /** the name of the large(default: 24x24 px) chevron-right image (used for toolbar buttons or icons). */
    public static final String CHEVRON_RIGHT_LARGE = "FontAwesome.chevron-right;size=24";

    /** the name of the small(default: 16x16 px) plus-circle image (used for menuitems or buttons). */
    public static final String PLUS_CIRCLE_SMALL = "FontAwesome.plus-circle";
    /** the name of the large(default: 24x24 px) plus-circle image (used for toolbar buttons or icons). */
    public static final String PLUS_CIRCLE_LARGE = "FontAwesome.plus-circle;size=24";

    /** the name of the small(default: 16x16 px) minus-circle image (used for menuitems or buttons). */
    public static final String MINUS_CIRCLE_SMALL = "FontAwesome.minus-circle";
    /** the name of the large(default: 24x24 px) minus-circle image (used for toolbar buttons or icons). */
    public static final String MINUS_CIRCLE_LARGE = "FontAwesome.minus-circle;size=24";

    /** the name of the small(default: 16x16 px) times-circle image (used for menuitems or buttons). */
    public static final String TIMES_CIRCLE_SMALL = "FontAwesome.times-circle";
    /** the name of the large(default: 24x24 px) times-circle image (used for toolbar buttons or icons). */
    public static final String TIMES_CIRCLE_LARGE = "FontAwesome.times-circle;size=24";

    /** the name of the small(default: 16x16 px) check-circle image (used for menuitems or buttons). */
    public static final String CHECK_CIRCLE_SMALL = "FontAwesome.check-circle";
    /** the name of the large(default: 24x24 px) check-circle image (used for toolbar buttons or icons). */
    public static final String CHECK_CIRCLE_LARGE = "FontAwesome.check-circle;size=24";

    /** the name of the small(default: 16x16 px) question-circle image (used for menuitems or buttons). */
    public static final String QUESTION_CIRCLE_SMALL = "FontAwesome.question-circle";
    /** the name of the large(default: 24x24 px) question-circle image (used for toolbar buttons or icons). */
    public static final String QUESTION_CIRCLE_LARGE = "FontAwesome.question-circle;size=24";

    /** the name of the small(default: 16x16 px) info-circle image (used for menuitems or buttons). */
    public static final String INFO_CIRCLE_SMALL = "FontAwesome.info-circle";
    /** the name of the large(default: 24x24 px) info-circle image (used for toolbar buttons or icons). */
    public static final String INFO_CIRCLE_LARGE = "FontAwesome.info-circle;size=24";

    /** the name of the small(default: 16x16 px) crosshairs image (used for menuitems or buttons). */
    public static final String CROSSHAIRS_SMALL = "FontAwesome.crosshairs";
    /** the name of the large(default: 24x24 px) crosshairs image (used for toolbar buttons or icons). */
    public static final String CROSSHAIRS_LARGE = "FontAwesome.crosshairs;size=24";

    /** the name of the small(default: 16x16 px) times-circle-o image (used for menuitems or buttons). */
    public static final String TIMES_CIRCLE_O_SMALL = "FontAwesome.times-circle-o";
    /** the name of the large(default: 24x24 px) times-circle-o image (used for toolbar buttons or icons). */
    public static final String TIMES_CIRCLE_O_LARGE = "FontAwesome.times-circle-o;size=24";

    /** the name of the small(default: 16x16 px) check-circle-o image (used for menuitems or buttons). */
    public static final String CHECK_CIRCLE_O_SMALL = "FontAwesome.check-circle-o";
    /** the name of the large(default: 24x24 px) check-circle-o image (used for toolbar buttons or icons). */
    public static final String CHECK_CIRCLE_O_LARGE = "FontAwesome.check-circle-o;size=24";

    /** the name of the small(default: 16x16 px) ban image (used for menuitems or buttons). */
    public static final String BAN_SMALL = "FontAwesome.ban";
    /** the name of the large(default: 24x24 px) ban image (used for toolbar buttons or icons). */
    public static final String BAN_LARGE = "FontAwesome.ban;size=24";

    /** the name of the small(default: 16x16 px) arrow-left image (used for menuitems or buttons). */
    public static final String ARROW_LEFT_SMALL = "FontAwesome.arrow-left";
    /** the name of the large(default: 24x24 px) arrow-left image (used for toolbar buttons or icons). */
    public static final String ARROW_LEFT_LARGE = "FontAwesome.arrow-left;size=24";

    /** the name of the small(default: 16x16 px) arrow-right image (used for menuitems or buttons). */
    public static final String ARROW_RIGHT_SMALL = "FontAwesome.arrow-right";
    /** the name of the large(default: 24x24 px) arrow-right image (used for toolbar buttons or icons). */
    public static final String ARROW_RIGHT_LARGE = "FontAwesome.arrow-right;size=24";

    /** the name of the small(default: 16x16 px) arrow-up image (used for menuitems or buttons). */
    public static final String ARROW_UP_SMALL = "FontAwesome.arrow-up";
    /** the name of the large(default: 24x24 px) arrow-up image (used for toolbar buttons or icons). */
    public static final String ARROW_UP_LARGE = "FontAwesome.arrow-up;size=24";

    /** the name of the small(default: 16x16 px) arrow-down image (used for menuitems or buttons). */
    public static final String ARROW_DOWN_SMALL = "FontAwesome.arrow-down";
    /** the name of the large(default: 24x24 px) arrow-down image (used for toolbar buttons or icons). */
    public static final String ARROW_DOWN_LARGE = "FontAwesome.arrow-down;size=24";

    /** the name of the small(default: 16x16 px) mail-forward image (used for menuitems or buttons). */
    public static final String MAIL_FORWARD_SMALL = "FontAwesome.mail-forward";
    /** the name of the large(default: 24x24 px) mail-forward image (used for toolbar buttons or icons). */
    public static final String MAIL_FORWARD_LARGE = "FontAwesome.mail-forward;size=24";

    /** the name of the small(default: 16x16 px) share image (used for menuitems or buttons). */
    public static final String SHARE_SMALL = "FontAwesome.share";
    /** the name of the large(default: 24x24 px) share image (used for toolbar buttons or icons). */
    public static final String SHARE_LARGE = "FontAwesome.share;size=24";

    /** the name of the small(default: 16x16 px) expand image (used for menuitems or buttons). */
    public static final String EXPAND_SMALL = "FontAwesome.expand";
    /** the name of the large(default: 24x24 px) expand image (used for toolbar buttons or icons). */
    public static final String EXPAND_LARGE = "FontAwesome.expand;size=24";

    /** the name of the small(default: 16x16 px) compress image (used for menuitems or buttons). */
    public static final String COMPRESS_SMALL = "FontAwesome.compress";
    /** the name of the large(default: 24x24 px) compress image (used for toolbar buttons or icons). */
    public static final String COMPRESS_LARGE = "FontAwesome.compress;size=24";

    /** the name of the small(default: 16x16 px) plus image (used for menuitems or buttons). */
    public static final String PLUS_SMALL = "FontAwesome.plus";
    /** the name of the large(default: 24x24 px) plus image (used for toolbar buttons or icons). */
    public static final String PLUS_LARGE = "FontAwesome.plus;size=24";

    /** the name of the small(default: 16x16 px) minus image (used for menuitems or buttons). */
    public static final String MINUS_SMALL = "FontAwesome.minus";
    /** the name of the large(default: 24x24 px) minus image (used for toolbar buttons or icons). */
    public static final String MINUS_LARGE = "FontAwesome.minus;size=24";

    /** the name of the small(default: 16x16 px) asterisk image (used for menuitems or buttons). */
    public static final String ASTERISK_SMALL = "FontAwesome.asterisk";
    /** the name of the large(default: 24x24 px) asterisk image (used for toolbar buttons or icons). */
    public static final String ASTERISK_LARGE = "FontAwesome.asterisk;size=24";

    /** the name of the small(default: 16x16 px) exclamation-circle image (used for menuitems or buttons). */
    public static final String EXCLAMATION_CIRCLE_SMALL = "FontAwesome.exclamation-circle";
    /** the name of the large(default: 24x24 px) exclamation-circle image (used for toolbar buttons or icons). */
    public static final String EXCLAMATION_CIRCLE_LARGE = "FontAwesome.exclamation-circle;size=24";

    /** the name of the small(default: 16x16 px) gift image (used for menuitems or buttons). */
    public static final String GIFT_SMALL = "FontAwesome.gift";
    /** the name of the large(default: 24x24 px) gift image (used for toolbar buttons or icons). */
    public static final String GIFT_LARGE = "FontAwesome.gift;size=24";

    /** the name of the small(default: 16x16 px) leaf image (used for menuitems or buttons). */
    public static final String LEAF_SMALL = "FontAwesome.leaf";
    /** the name of the large(default: 24x24 px) leaf image (used for toolbar buttons or icons). */
    public static final String LEAF_LARGE = "FontAwesome.leaf;size=24";

    /** the name of the small(default: 16x16 px) fire image (used for menuitems or buttons). */
    public static final String FIRE_SMALL = "FontAwesome.fire";
    /** the name of the large(default: 24x24 px) fire image (used for toolbar buttons or icons). */
    public static final String FIRE_LARGE = "FontAwesome.fire;size=24";

    /** the name of the small(default: 16x16 px) eye image (used for menuitems or buttons). */
    public static final String EYE_SMALL = "FontAwesome.eye";
    /** the name of the large(default: 24x24 px) eye image (used for toolbar buttons or icons). */
    public static final String EYE_LARGE = "FontAwesome.eye;size=24";

    /** the name of the small(default: 16x16 px) eye-slash image (used for menuitems or buttons). */
    public static final String EYE_SLASH_SMALL = "FontAwesome.eye-slash";
    /** the name of the large(default: 24x24 px) eye-slash image (used for toolbar buttons or icons). */
    public static final String EYE_SLASH_LARGE = "FontAwesome.eye-slash;size=24";

    /** the name of the small(default: 16x16 px) warning image (used for menuitems or buttons). */
    public static final String WARNING_SMALL = "FontAwesome.warning";
    /** the name of the large(default: 24x24 px) warning image (used for toolbar buttons or icons). */
    public static final String WARNING_LARGE = "FontAwesome.warning;size=24";

    /** the name of the small(default: 16x16 px) exclamation-triangle image (used for menuitems or buttons). */
    public static final String EXCLAMATION_TRIANGLE_SMALL = "FontAwesome.exclamation-triangle";
    /** the name of the large(default: 24x24 px) exclamation-triangle image (used for toolbar buttons or icons). */
    public static final String EXCLAMATION_TRIANGLE_LARGE = "FontAwesome.exclamation-triangle;size=24";

    /** the name of the small(default: 16x16 px) plane image (used for menuitems or buttons). */
    public static final String PLANE_SMALL = "FontAwesome.plane";
    /** the name of the large(default: 24x24 px) plane image (used for toolbar buttons or icons). */
    public static final String PLANE_LARGE = "FontAwesome.plane;size=24";

    /** the name of the small(default: 16x16 px) calendar image (used for menuitems or buttons). */
    public static final String CALENDAR_SMALL = "FontAwesome.calendar";
    /** the name of the large(default: 24x24 px) calendar image (used for toolbar buttons or icons). */
    public static final String CALENDAR_LARGE = "FontAwesome.calendar;size=24";

    /** the name of the small(default: 16x16 px) random image (used for menuitems or buttons). */
    public static final String RANDOM_SMALL = "FontAwesome.random";
    /** the name of the large(default: 24x24 px) random image (used for toolbar buttons or icons). */
    public static final String RANDOM_LARGE = "FontAwesome.random;size=24";

    /** the name of the small(default: 16x16 px) comment image (used for menuitems or buttons). */
    public static final String COMMENT_SMALL = "FontAwesome.comment";
    /** the name of the large(default: 24x24 px) comment image (used for toolbar buttons or icons). */
    public static final String COMMENT_LARGE = "FontAwesome.comment;size=24";

    /** the name of the small(default: 16x16 px) magnet image (used for menuitems or buttons). */
    public static final String MAGNET_SMALL = "FontAwesome.magnet";
    /** the name of the large(default: 24x24 px) magnet image (used for toolbar buttons or icons). */
    public static final String MAGNET_LARGE = "FontAwesome.magnet;size=24";

    /** the name of the small(default: 16x16 px) chevron-up image (used for menuitems or buttons). */
    public static final String CHEVRON_UP_SMALL = "FontAwesome.chevron-up";
    /** the name of the large(default: 24x24 px) chevron-up image (used for toolbar buttons or icons). */
    public static final String CHEVRON_UP_LARGE = "FontAwesome.chevron-up;size=24";

    /** the name of the small(default: 16x16 px) chevron-down image (used for menuitems or buttons). */
    public static final String CHEVRON_DOWN_SMALL = "FontAwesome.chevron-down";
    /** the name of the large(default: 24x24 px) chevron-down image (used for toolbar buttons or icons). */
    public static final String CHEVRON_DOWN_LARGE = "FontAwesome.chevron-down;size=24";

    /** the name of the small(default: 16x16 px) retweet image (used for menuitems or buttons). */
    public static final String RETWEET_SMALL = "FontAwesome.retweet";
    /** the name of the large(default: 24x24 px) retweet image (used for toolbar buttons or icons). */
    public static final String RETWEET_LARGE = "FontAwesome.retweet;size=24";

    /** the name of the small(default: 16x16 px) shopping-cart image (used for menuitems or buttons). */
    public static final String SHOPPING_CART_SMALL = "FontAwesome.shopping-cart";
    /** the name of the large(default: 24x24 px) shopping-cart image (used for toolbar buttons or icons). */
    public static final String SHOPPING_CART_LARGE = "FontAwesome.shopping-cart;size=24";

    /** the name of the small(default: 16x16 px) folder image (used for menuitems or buttons). */
    public static final String FOLDER_SMALL = "FontAwesome.folder";
    /** the name of the large(default: 24x24 px) folder image (used for toolbar buttons or icons). */
    public static final String FOLDER_LARGE = "FontAwesome.folder;size=24";

    /** the name of the small(default: 16x16 px) folder-open image (used for menuitems or buttons). */
    public static final String FOLDER_OPEN_SMALL = "FontAwesome.folder-open";
    /** the name of the large(default: 24x24 px) folder-open image (used for toolbar buttons or icons). */
    public static final String FOLDER_OPEN_LARGE = "FontAwesome.folder-open;size=24";

    /** the name of the small(default: 16x16 px) arrows-v image (used for menuitems or buttons). */
    public static final String ARROWS_V_SMALL = "FontAwesome.arrows-v";
    /** the name of the large(default: 24x24 px) arrows-v image (used for toolbar buttons or icons). */
    public static final String ARROWS_V_LARGE = "FontAwesome.arrows-v;size=24";

    /** the name of the small(default: 16x16 px) arrows-h image (used for menuitems or buttons). */
    public static final String ARROWS_H_SMALL = "FontAwesome.arrows-h";
    /** the name of the large(default: 24x24 px) arrows-h image (used for toolbar buttons or icons). */
    public static final String ARROWS_H_LARGE = "FontAwesome.arrows-h;size=24";

    /** the name of the small(default: 16x16 px) bar-chart-o image (used for menuitems or buttons). */
    public static final String BAR_CHART_O_SMALL = "FontAwesome.bar-chart-o";
    /** the name of the large(default: 24x24 px) bar-chart-o image (used for toolbar buttons or icons). */
    public static final String BAR_CHART_O_LARGE = "FontAwesome.bar-chart-o;size=24";

    /** the name of the small(default: 16x16 px) bar-chart image (used for menuitems or buttons). */
    public static final String BAR_CHART_SMALL = "FontAwesome.bar-chart";
    /** the name of the large(default: 24x24 px) bar-chart image (used for toolbar buttons or icons). */
    public static final String BAR_CHART_LARGE = "FontAwesome.bar-chart;size=24";

    /** the name of the small(default: 16x16 px) twitter-square image (used for menuitems or buttons). */
    public static final String TWITTER_SQUARE_SMALL = "FontAwesome.twitter-square";
    /** the name of the large(default: 24x24 px) twitter-square image (used for toolbar buttons or icons). */
    public static final String TWITTER_SQUARE_LARGE = "FontAwesome.twitter-square;size=24";

    /** the name of the small(default: 16x16 px) facebook-square image (used for menuitems or buttons). */
    public static final String FACEBOOK_SQUARE_SMALL = "FontAwesome.facebook-square";
    /** the name of the large(default: 24x24 px) facebook-square image (used for toolbar buttons or icons). */
    public static final String FACEBOOK_SQUARE_LARGE = "FontAwesome.facebook-square;size=24";

    /** the name of the small(default: 16x16 px) camera-retro image (used for menuitems or buttons). */
    public static final String CAMERA_RETRO_SMALL = "FontAwesome.camera-retro";
    /** the name of the large(default: 24x24 px) camera-retro image (used for toolbar buttons or icons). */
    public static final String CAMERA_RETRO_LARGE = "FontAwesome.camera-retro;size=24";

    /** the name of the small(default: 16x16 px) key image (used for menuitems or buttons). */
    public static final String KEY_SMALL = "FontAwesome.key";
    /** the name of the large(default: 24x24 px) key image (used for toolbar buttons or icons). */
    public static final String KEY_LARGE = "FontAwesome.key;size=24";

    /** the name of the small(default: 16x16 px) gears image (used for menuitems or buttons). */
    public static final String GEARS_SMALL = "FontAwesome.gears";
    /** the name of the large(default: 24x24 px) gears image (used for toolbar buttons or icons). */
    public static final String GEARS_LARGE = "FontAwesome.gears;size=24";

    /** the name of the small(default: 16x16 px) cogs image (used for menuitems or buttons). */
    public static final String COGS_SMALL = "FontAwesome.cogs";
    /** the name of the large(default: 24x24 px) cogs image (used for toolbar buttons or icons). */
    public static final String COGS_LARGE = "FontAwesome.cogs;size=24";

    /** the name of the small(default: 16x16 px) comments image (used for menuitems or buttons). */
    public static final String COMMENTS_SMALL = "FontAwesome.comments";
    /** the name of the large(default: 24x24 px) comments image (used for toolbar buttons or icons). */
    public static final String COMMENTS_LARGE = "FontAwesome.comments;size=24";

    /** the name of the small(default: 16x16 px) thumbs-o-up image (used for menuitems or buttons). */
    public static final String THUMBS_O_UP_SMALL = "FontAwesome.thumbs-o-up";
    /** the name of the large(default: 24x24 px) thumbs-o-up image (used for toolbar buttons or icons). */
    public static final String THUMBS_O_UP_LARGE = "FontAwesome.thumbs-o-up;size=24";

    /** the name of the small(default: 16x16 px) thumbs-o-down image (used for menuitems or buttons). */
    public static final String THUMBS_O_DOWN_SMALL = "FontAwesome.thumbs-o-down";
    /** the name of the large(default: 24x24 px) thumbs-o-down image (used for toolbar buttons or icons). */
    public static final String THUMBS_O_DOWN_LARGE = "FontAwesome.thumbs-o-down;size=24";

    /** the name of the small(default: 16x16 px) star-half image (used for menuitems or buttons). */
    public static final String STAR_HALF_SMALL = "FontAwesome.star-half";
    /** the name of the large(default: 24x24 px) star-half image (used for toolbar buttons or icons). */
    public static final String STAR_HALF_LARGE = "FontAwesome.star-half;size=24";

    /** the name of the small(default: 16x16 px) heart-o image (used for menuitems or buttons). */
    public static final String HEART_O_SMALL = "FontAwesome.heart-o";
    /** the name of the large(default: 24x24 px) heart-o image (used for toolbar buttons or icons). */
    public static final String HEART_O_LARGE = "FontAwesome.heart-o;size=24";

    /** the name of the small(default: 16x16 px) sign-out image (used for menuitems or buttons). */
    public static final String SIGN_OUT_SMALL = "FontAwesome.sign-out";
    /** the name of the large(default: 24x24 px) sign-out image (used for toolbar buttons or icons). */
    public static final String SIGN_OUT_LARGE = "FontAwesome.sign-out;size=24";

    /** the name of the small(default: 16x16 px) linkedin-square image (used for menuitems or buttons). */
    public static final String LINKEDIN_SQUARE_SMALL = "FontAwesome.linkedin-square";
    /** the name of the large(default: 24x24 px) linkedin-square image (used for toolbar buttons or icons). */
    public static final String LINKEDIN_SQUARE_LARGE = "FontAwesome.linkedin-square;size=24";

    /** the name of the small(default: 16x16 px) thumb-tack image (used for menuitems or buttons). */
    public static final String THUMB_TACK_SMALL = "FontAwesome.thumb-tack";
    /** the name of the large(default: 24x24 px) thumb-tack image (used for toolbar buttons or icons). */
    public static final String THUMB_TACK_LARGE = "FontAwesome.thumb-tack;size=24";

    /** the name of the small(default: 16x16 px) external-link image (used for menuitems or buttons). */
    public static final String EXTERNAL_LINK_SMALL = "FontAwesome.external-link";
    /** the name of the large(default: 24x24 px) external-link image (used for toolbar buttons or icons). */
    public static final String EXTERNAL_LINK_LARGE = "FontAwesome.external-link;size=24";

    /** the name of the small(default: 16x16 px) sign-in image (used for menuitems or buttons). */
    public static final String SIGN_IN_SMALL = "FontAwesome.sign-in";
    /** the name of the large(default: 24x24 px) sign-in image (used for toolbar buttons or icons). */
    public static final String SIGN_IN_LARGE = "FontAwesome.sign-in;size=24";

    /** the name of the small(default: 16x16 px) trophy image (used for menuitems or buttons). */
    public static final String TROPHY_SMALL = "FontAwesome.trophy";
    /** the name of the large(default: 24x24 px) trophy image (used for toolbar buttons or icons). */
    public static final String TROPHY_LARGE = "FontAwesome.trophy;size=24";

    /** the name of the small(default: 16x16 px) github-square image (used for menuitems or buttons). */
    public static final String GITHUB_SQUARE_SMALL = "FontAwesome.github-square";
    /** the name of the large(default: 24x24 px) github-square image (used for toolbar buttons or icons). */
    public static final String GITHUB_SQUARE_LARGE = "FontAwesome.github-square;size=24";

    /** the name of the small(default: 16x16 px) upload image (used for menuitems or buttons). */
    public static final String UPLOAD_SMALL = "FontAwesome.upload";
    /** the name of the large(default: 24x24 px) upload image (used for toolbar buttons or icons). */
    public static final String UPLOAD_LARGE = "FontAwesome.upload;size=24";

    /** the name of the small(default: 16x16 px) lemon-o image (used for menuitems or buttons). */
    public static final String LEMON_O_SMALL = "FontAwesome.lemon-o";
    /** the name of the large(default: 24x24 px) lemon-o image (used for toolbar buttons or icons). */
    public static final String LEMON_O_LARGE = "FontAwesome.lemon-o;size=24";

    /** the name of the small(default: 16x16 px) phone image (used for menuitems or buttons). */
    public static final String PHONE_SMALL = "FontAwesome.phone";
    /** the name of the large(default: 24x24 px) phone image (used for toolbar buttons or icons). */
    public static final String PHONE_LARGE = "FontAwesome.phone;size=24";

    /** the name of the small(default: 16x16 px) square-o image (used for menuitems or buttons). */
    public static final String SQUARE_O_SMALL = "FontAwesome.square-o";
    /** the name of the large(default: 24x24 px) square-o image (used for toolbar buttons or icons). */
    public static final String SQUARE_O_LARGE = "FontAwesome.square-o;size=24";

    /** the name of the small(default: 16x16 px) bookmark-o image (used for menuitems or buttons). */
    public static final String BOOKMARK_O_SMALL = "FontAwesome.bookmark-o";
    /** the name of the large(default: 24x24 px) bookmark-o image (used for toolbar buttons or icons). */
    public static final String BOOKMARK_O_LARGE = "FontAwesome.bookmark-o;size=24";

    /** the name of the small(default: 16x16 px) phone-square image (used for menuitems or buttons). */
    public static final String PHONE_SQUARE_SMALL = "FontAwesome.phone-square";
    /** the name of the large(default: 24x24 px) phone-square image (used for toolbar buttons or icons). */
    public static final String PHONE_SQUARE_LARGE = "FontAwesome.phone-square;size=24";

    /** the name of the small(default: 16x16 px) twitter image (used for menuitems or buttons). */
    public static final String TWITTER_SMALL = "FontAwesome.twitter";
    /** the name of the large(default: 24x24 px) twitter image (used for toolbar buttons or icons). */
    public static final String TWITTER_LARGE = "FontAwesome.twitter;size=24";

    /** the name of the small(default: 16x16 px) facebook-f image (used for menuitems or buttons). */
    public static final String FACEBOOK_F_SMALL = "FontAwesome.facebook-f";
    /** the name of the large(default: 24x24 px) facebook-f image (used for toolbar buttons or icons). */
    public static final String FACEBOOK_F_LARGE = "FontAwesome.facebook-f;size=24";

    /** the name of the small(default: 16x16 px) facebook image (used for menuitems or buttons). */
    public static final String FACEBOOK_SMALL = "FontAwesome.facebook";
    /** the name of the large(default: 24x24 px) facebook image (used for toolbar buttons or icons). */
    public static final String FACEBOOK_LARGE = "FontAwesome.facebook;size=24";

    /** the name of the small(default: 16x16 px) github image (used for menuitems or buttons). */
    public static final String GITHUB_SMALL = "FontAwesome.github";
    /** the name of the large(default: 24x24 px) github image (used for toolbar buttons or icons). */
    public static final String GITHUB_LARGE = "FontAwesome.github;size=24";

    /** the name of the small(default: 16x16 px) unlock image (used for menuitems or buttons). */
    public static final String UNLOCK_SMALL = "FontAwesome.unlock";
    /** the name of the large(default: 24x24 px) unlock image (used for toolbar buttons or icons). */
    public static final String UNLOCK_LARGE = "FontAwesome.unlock;size=24";

    /** the name of the small(default: 16x16 px) credit-card image (used for menuitems or buttons). */
    public static final String CREDIT_CARD_SMALL = "FontAwesome.credit-card";
    /** the name of the large(default: 24x24 px) credit-card image (used for toolbar buttons or icons). */
    public static final String CREDIT_CARD_LARGE = "FontAwesome.credit-card;size=24";

    /** the name of the small(default: 16x16 px) feed image (used for menuitems or buttons). */
    public static final String FEED_SMALL = "FontAwesome.feed";
    /** the name of the large(default: 24x24 px) feed image (used for toolbar buttons or icons). */
    public static final String FEED_LARGE = "FontAwesome.feed;size=24";

    /** the name of the small(default: 16x16 px) rss image (used for menuitems or buttons). */
    public static final String RSS_SMALL = "FontAwesome.rss";
    /** the name of the large(default: 24x24 px) rss image (used for toolbar buttons or icons). */
    public static final String RSS_LARGE = "FontAwesome.rss;size=24";

    /** the name of the small(default: 16x16 px) hdd-o image (used for menuitems or buttons). */
    public static final String HDD_O_SMALL = "FontAwesome.hdd-o";
    /** the name of the large(default: 24x24 px) hdd-o image (used for toolbar buttons or icons). */
    public static final String HDD_O_LARGE = "FontAwesome.hdd-o;size=24";

    /** the name of the small(default: 16x16 px) bullhorn image (used for menuitems or buttons). */
    public static final String BULLHORN_SMALL = "FontAwesome.bullhorn";
    /** the name of the large(default: 24x24 px) bullhorn image (used for toolbar buttons or icons). */
    public static final String BULLHORN_LARGE = "FontAwesome.bullhorn;size=24";

    /** the name of the small(default: 16x16 px) bell image (used for menuitems or buttons). */
    public static final String BELL_SMALL = "FontAwesome.bell";
    /** the name of the large(default: 24x24 px) bell image (used for toolbar buttons or icons). */
    public static final String BELL_LARGE = "FontAwesome.bell;size=24";

    /** the name of the small(default: 16x16 px) certificate image (used for menuitems or buttons). */
    public static final String CERTIFICATE_SMALL = "FontAwesome.certificate";
    /** the name of the large(default: 24x24 px) certificate image (used for toolbar buttons or icons). */
    public static final String CERTIFICATE_LARGE = "FontAwesome.certificate;size=24";

    /** the name of the small(default: 16x16 px) hand-o-right image (used for menuitems or buttons). */
    public static final String HAND_O_RIGHT_SMALL = "FontAwesome.hand-o-right";
    /** the name of the large(default: 24x24 px) hand-o-right image (used for toolbar buttons or icons). */
    public static final String HAND_O_RIGHT_LARGE = "FontAwesome.hand-o-right;size=24";

    /** the name of the small(default: 16x16 px) hand-o-left image (used for menuitems or buttons). */
    public static final String HAND_O_LEFT_SMALL = "FontAwesome.hand-o-left";
    /** the name of the large(default: 24x24 px) hand-o-left image (used for toolbar buttons or icons). */
    public static final String HAND_O_LEFT_LARGE = "FontAwesome.hand-o-left;size=24";

    /** the name of the small(default: 16x16 px) hand-o-up image (used for menuitems or buttons). */
    public static final String HAND_O_UP_SMALL = "FontAwesome.hand-o-up";
    /** the name of the large(default: 24x24 px) hand-o-up image (used for toolbar buttons or icons). */
    public static final String HAND_O_UP_LARGE = "FontAwesome.hand-o-up;size=24";

    /** the name of the small(default: 16x16 px) hand-o-down image (used for menuitems or buttons). */
    public static final String HAND_O_DOWN_SMALL = "FontAwesome.hand-o-down";
    /** the name of the large(default: 24x24 px) hand-o-down image (used for toolbar buttons or icons). */
    public static final String HAND_O_DOWN_LARGE = "FontAwesome.hand-o-down;size=24";

    /** the name of the small(default: 16x16 px) arrow-circle-left image (used for menuitems or buttons). */
    public static final String ARROW_CIRCLE_LEFT_SMALL = "FontAwesome.arrow-circle-left";
    /** the name of the large(default: 24x24 px) arrow-circle-left image (used for toolbar buttons or icons). */
    public static final String ARROW_CIRCLE_LEFT_LARGE = "FontAwesome.arrow-circle-left;size=24";

    /** the name of the small(default: 16x16 px) arrow-circle-right image (used for menuitems or buttons). */
    public static final String ARROW_CIRCLE_RIGHT_SMALL = "FontAwesome.arrow-circle-right";
    /** the name of the large(default: 24x24 px) arrow-circle-right image (used for toolbar buttons or icons). */
    public static final String ARROW_CIRCLE_RIGHT_LARGE = "FontAwesome.arrow-circle-right;size=24";

    /** the name of the small(default: 16x16 px) arrow-circle-up image (used for menuitems or buttons). */
    public static final String ARROW_CIRCLE_UP_SMALL = "FontAwesome.arrow-circle-up";
    /** the name of the large(default: 24x24 px) arrow-circle-up image (used for toolbar buttons or icons). */
    public static final String ARROW_CIRCLE_UP_LARGE = "FontAwesome.arrow-circle-up;size=24";

    /** the name of the small(default: 16x16 px) arrow-circle-down image (used for menuitems or buttons). */
    public static final String ARROW_CIRCLE_DOWN_SMALL = "FontAwesome.arrow-circle-down";
    /** the name of the large(default: 24x24 px) arrow-circle-down image (used for toolbar buttons or icons). */
    public static final String ARROW_CIRCLE_DOWN_LARGE = "FontAwesome.arrow-circle-down;size=24";

    /** the name of the small(default: 16x16 px) globe image (used for menuitems or buttons). */
    public static final String GLOBE_SMALL = "FontAwesome.globe";
    /** the name of the large(default: 24x24 px) globe image (used for toolbar buttons or icons). */
    public static final String GLOBE_LARGE = "FontAwesome.globe;size=24";

    /** the name of the small(default: 16x16 px) wrench image (used for menuitems or buttons). */
    public static final String WRENCH_SMALL = "FontAwesome.wrench";
    /** the name of the large(default: 24x24 px) wrench image (used for toolbar buttons or icons). */
    public static final String WRENCH_LARGE = "FontAwesome.wrench;size=24";

    /** the name of the small(default: 16x16 px) tasks image (used for menuitems or buttons). */
    public static final String TASKS_SMALL = "FontAwesome.tasks";
    /** the name of the large(default: 24x24 px) tasks image (used for toolbar buttons or icons). */
    public static final String TASKS_LARGE = "FontAwesome.tasks;size=24";

    /** the name of the small(default: 16x16 px) filter image (used for menuitems or buttons). */
    public static final String FILTER_SMALL = "FontAwesome.filter";
    /** the name of the large(default: 24x24 px) filter image (used for toolbar buttons or icons). */
    public static final String FILTER_LARGE = "FontAwesome.filter;size=24";

    /** the name of the small(default: 16x16 px) briefcase image (used for menuitems or buttons). */
    public static final String BRIEFCASE_SMALL = "FontAwesome.briefcase";
    /** the name of the large(default: 24x24 px) briefcase image (used for toolbar buttons or icons). */
    public static final String BRIEFCASE_LARGE = "FontAwesome.briefcase;size=24";

    /** the name of the small(default: 16x16 px) arrows-alt image (used for menuitems or buttons). */
    public static final String ARROWS_ALT_SMALL = "FontAwesome.arrows-alt";
    /** the name of the large(default: 24x24 px) arrows-alt image (used for toolbar buttons or icons). */
    public static final String ARROWS_ALT_LARGE = "FontAwesome.arrows-alt;size=24";

    /** the name of the small(default: 16x16 px) group image (used for menuitems or buttons). */
    public static final String GROUP_SMALL = "FontAwesome.group";
    /** the name of the large(default: 24x24 px) group image (used for toolbar buttons or icons). */
    public static final String GROUP_LARGE = "FontAwesome.group;size=24";

    /** the name of the small(default: 16x16 px) users image (used for menuitems or buttons). */
    public static final String USERS_SMALL = "FontAwesome.users";
    /** the name of the large(default: 24x24 px) users image (used for toolbar buttons or icons). */
    public static final String USERS_LARGE = "FontAwesome.users;size=24";

    /** the name of the small(default: 16x16 px) chain image (used for menuitems or buttons). */
    public static final String CHAIN_SMALL = "FontAwesome.chain";
    /** the name of the large(default: 24x24 px) chain image (used for toolbar buttons or icons). */
    public static final String CHAIN_LARGE = "FontAwesome.chain;size=24";

    /** the name of the small(default: 16x16 px) link image (used for menuitems or buttons). */
    public static final String LINK_SMALL = "FontAwesome.link";
    /** the name of the large(default: 24x24 px) link image (used for toolbar buttons or icons). */
    public static final String LINK_LARGE = "FontAwesome.link;size=24";

    /** the name of the small(default: 16x16 px) cloud image (used for menuitems or buttons). */
    public static final String CLOUD_SMALL = "FontAwesome.cloud";
    /** the name of the large(default: 24x24 px) cloud image (used for toolbar buttons or icons). */
    public static final String CLOUD_LARGE = "FontAwesome.cloud;size=24";

    /** the name of the small(default: 16x16 px) flask image (used for menuitems or buttons). */
    public static final String FLASK_SMALL = "FontAwesome.flask";
    /** the name of the large(default: 24x24 px) flask image (used for toolbar buttons or icons). */
    public static final String FLASK_LARGE = "FontAwesome.flask;size=24";

    /** the name of the small(default: 16x16 px) cut image (used for menuitems or buttons). */
    public static final String CUT_SMALL = "FontAwesome.cut";
    /** the name of the large(default: 24x24 px) cut image (used for toolbar buttons or icons). */
    public static final String CUT_LARGE = "FontAwesome.cut;size=24";

    /** the name of the small(default: 16x16 px) scissors image (used for menuitems or buttons). */
    public static final String SCISSORS_SMALL = "FontAwesome.scissors";
    /** the name of the large(default: 24x24 px) scissors image (used for toolbar buttons or icons). */
    public static final String SCISSORS_LARGE = "FontAwesome.scissors;size=24";

    /** the name of the small(default: 16x16 px) copy image (used for menuitems or buttons). */
    public static final String COPY_SMALL = "FontAwesome.copy";
    /** the name of the large(default: 24x24 px) copy image (used for toolbar buttons or icons). */
    public static final String COPY_LARGE = "FontAwesome.copy;size=24";

    /** the name of the small(default: 16x16 px) files-o image (used for menuitems or buttons). */
    public static final String FILES_O_SMALL = "FontAwesome.files-o";
    /** the name of the large(default: 24x24 px) files-o image (used for toolbar buttons or icons). */
    public static final String FILES_O_LARGE = "FontAwesome.files-o;size=24";

    /** the name of the small(default: 16x16 px) paperclip image (used for menuitems or buttons). */
    public static final String PAPERCLIP_SMALL = "FontAwesome.paperclip";
    /** the name of the large(default: 24x24 px) paperclip image (used for toolbar buttons or icons). */
    public static final String PAPERCLIP_LARGE = "FontAwesome.paperclip;size=24";

    /** the name of the small(default: 16x16 px) save image (used for menuitems or buttons). */
    public static final String SAVE_SMALL = "FontAwesome.save";
    /** the name of the large(default: 24x24 px) save image (used for toolbar buttons or icons). */
    public static final String SAVE_LARGE = "FontAwesome.save;size=24";

    /** the name of the small(default: 16x16 px) floppy-o image (used for menuitems or buttons). */
    public static final String FLOPPY_O_SMALL = "FontAwesome.floppy-o";
    /** the name of the large(default: 24x24 px) floppy-o image (used for toolbar buttons or icons). */
    public static final String FLOPPY_O_LARGE = "FontAwesome.floppy-o;size=24";

    /** the name of the small(default: 16x16 px) square image (used for menuitems or buttons). */
    public static final String SQUARE_SMALL = "FontAwesome.square";
    /** the name of the large(default: 24x24 px) square image (used for toolbar buttons or icons). */
    public static final String SQUARE_LARGE = "FontAwesome.square;size=24";

    /** the name of the small(default: 16x16 px) navicon image (used for menuitems or buttons). */
    public static final String NAVICON_SMALL = "FontAwesome.navicon";
    /** the name of the large(default: 24x24 px) navicon image (used for toolbar buttons or icons). */
    public static final String NAVICON_LARGE = "FontAwesome.navicon;size=24";

    /** the name of the small(default: 16x16 px) reorder image (used for menuitems or buttons). */
    public static final String REORDER_SMALL = "FontAwesome.reorder";
    /** the name of the large(default: 24x24 px) reorder image (used for toolbar buttons or icons). */
    public static final String REORDER_LARGE = "FontAwesome.reorder;size=24";

    /** the name of the small(default: 16x16 px) bars image (used for menuitems or buttons). */
    public static final String BARS_SMALL = "FontAwesome.bars";
    /** the name of the large(default: 24x24 px) bars image (used for toolbar buttons or icons). */
    public static final String BARS_LARGE = "FontAwesome.bars;size=24";

    /** the name of the small(default: 16x16 px) list-ul image (used for menuitems or buttons). */
    public static final String LIST_UL_SMALL = "FontAwesome.list-ul";
    /** the name of the large(default: 24x24 px) list-ul image (used for toolbar buttons or icons). */
    public static final String LIST_UL_LARGE = "FontAwesome.list-ul;size=24";

    /** the name of the small(default: 16x16 px) list-ol image (used for menuitems or buttons). */
    public static final String LIST_OL_SMALL = "FontAwesome.list-ol";
    /** the name of the large(default: 24x24 px) list-ol image (used for toolbar buttons or icons). */
    public static final String LIST_OL_LARGE = "FontAwesome.list-ol;size=24";

    /** the name of the small(default: 16x16 px) strikethrough image (used for menuitems or buttons). */
    public static final String STRIKETHROUGH_SMALL = "FontAwesome.strikethrough";
    /** the name of the large(default: 24x24 px) strikethrough image (used for toolbar buttons or icons). */
    public static final String STRIKETHROUGH_LARGE = "FontAwesome.strikethrough;size=24";

    /** the name of the small(default: 16x16 px) underline image (used for menuitems or buttons). */
    public static final String UNDERLINE_SMALL = "FontAwesome.underline";
    /** the name of the large(default: 24x24 px) underline image (used for toolbar buttons or icons). */
    public static final String UNDERLINE_LARGE = "FontAwesome.underline;size=24";

    /** the name of the small(default: 16x16 px) table image (used for menuitems or buttons). */
    public static final String TABLE_SMALL = "FontAwesome.table";
    /** the name of the large(default: 24x24 px) table image (used for toolbar buttons or icons). */
    public static final String TABLE_LARGE = "FontAwesome.table;size=24";

    /** the name of the small(default: 16x16 px) magic image (used for menuitems or buttons). */
    public static final String MAGIC_SMALL = "FontAwesome.magic";
    /** the name of the large(default: 24x24 px) magic image (used for toolbar buttons or icons). */
    public static final String MAGIC_LARGE = "FontAwesome.magic;size=24";

    /** the name of the small(default: 16x16 px) truck image (used for menuitems or buttons). */
    public static final String TRUCK_SMALL = "FontAwesome.truck";
    /** the name of the large(default: 24x24 px) truck image (used for toolbar buttons or icons). */
    public static final String TRUCK_LARGE = "FontAwesome.truck;size=24";

    /** the name of the small(default: 16x16 px) pinterest image (used for menuitems or buttons). */
    public static final String PINTEREST_SMALL = "FontAwesome.pinterest";
    /** the name of the large(default: 24x24 px) pinterest image (used for toolbar buttons or icons). */
    public static final String PINTEREST_LARGE = "FontAwesome.pinterest;size=24";

    /** the name of the small(default: 16x16 px) pinterest-square image (used for menuitems or buttons). */
    public static final String PINTEREST_SQUARE_SMALL = "FontAwesome.pinterest-square";
    /** the name of the large(default: 24x24 px) pinterest-square image (used for toolbar buttons or icons). */
    public static final String PINTEREST_SQUARE_LARGE = "FontAwesome.pinterest-square;size=24";

    /** the name of the small(default: 16x16 px) google-plus-square image (used for menuitems or buttons). */
    public static final String GOOGLE_PLUS_SQUARE_SMALL = "FontAwesome.google-plus-square";
    /** the name of the large(default: 24x24 px) google-plus-square image (used for toolbar buttons or icons). */
    public static final String GOOGLE_PLUS_SQUARE_LARGE = "FontAwesome.google-plus-square;size=24";

    /** the name of the small(default: 16x16 px) google-plus image (used for menuitems or buttons). */
    public static final String GOOGLE_PLUS_SMALL = "FontAwesome.google-plus";
    /** the name of the large(default: 24x24 px) google-plus image (used for toolbar buttons or icons). */
    public static final String GOOGLE_PLUS_LARGE = "FontAwesome.google-plus;size=24";

    /** the name of the small(default: 16x16 px) money image (used for menuitems or buttons). */
    public static final String MONEY_SMALL = "FontAwesome.money";
    /** the name of the large(default: 24x24 px) money image (used for toolbar buttons or icons). */
    public static final String MONEY_LARGE = "FontAwesome.money;size=24";

    /** the name of the small(default: 16x16 px) caret-down image (used for menuitems or buttons). */
    public static final String CARET_DOWN_SMALL = "FontAwesome.caret-down";
    /** the name of the large(default: 24x24 px) caret-down image (used for toolbar buttons or icons). */
    public static final String CARET_DOWN_LARGE = "FontAwesome.caret-down;size=24";

    /** the name of the small(default: 16x16 px) caret-up image (used for menuitems or buttons). */
    public static final String CARET_UP_SMALL = "FontAwesome.caret-up";
    /** the name of the large(default: 24x24 px) caret-up image (used for toolbar buttons or icons). */
    public static final String CARET_UP_LARGE = "FontAwesome.caret-up;size=24";

    /** the name of the small(default: 16x16 px) caret-left image (used for menuitems or buttons). */
    public static final String CARET_LEFT_SMALL = "FontAwesome.caret-left";
    /** the name of the large(default: 24x24 px) caret-left image (used for toolbar buttons or icons). */
    public static final String CARET_LEFT_LARGE = "FontAwesome.caret-left;size=24";

    /** the name of the small(default: 16x16 px) caret-right image (used for menuitems or buttons). */
    public static final String CARET_RIGHT_SMALL = "FontAwesome.caret-right";
    /** the name of the large(default: 24x24 px) caret-right image (used for toolbar buttons or icons). */
    public static final String CARET_RIGHT_LARGE = "FontAwesome.caret-right;size=24";

    /** the name of the small(default: 16x16 px) columns image (used for menuitems or buttons). */
    public static final String COLUMNS_SMALL = "FontAwesome.columns";
    /** the name of the large(default: 24x24 px) columns image (used for toolbar buttons or icons). */
    public static final String COLUMNS_LARGE = "FontAwesome.columns;size=24";

    /** the name of the small(default: 16x16 px) unsorted image (used for menuitems or buttons). */
    public static final String UNSORTED_SMALL = "FontAwesome.unsorted";
    /** the name of the large(default: 24x24 px) unsorted image (used for toolbar buttons or icons). */
    public static final String UNSORTED_LARGE = "FontAwesome.unsorted;size=24";

    /** the name of the small(default: 16x16 px) sort image (used for menuitems or buttons). */
    public static final String SORT_SMALL = "FontAwesome.sort";
    /** the name of the large(default: 24x24 px) sort image (used for toolbar buttons or icons). */
    public static final String SORT_LARGE = "FontAwesome.sort;size=24";

    /** the name of the small(default: 16x16 px) sort-down image (used for menuitems or buttons). */
    public static final String SORT_DOWN_SMALL = "FontAwesome.sort-down";
    /** the name of the large(default: 24x24 px) sort-down image (used for toolbar buttons or icons). */
    public static final String SORT_DOWN_LARGE = "FontAwesome.sort-down;size=24";

    /** the name of the small(default: 16x16 px) sort-desc image (used for menuitems or buttons). */
    public static final String SORT_DESC_SMALL = "FontAwesome.sort-desc";
    /** the name of the large(default: 24x24 px) sort-desc image (used for toolbar buttons or icons). */
    public static final String SORT_DESC_LARGE = "FontAwesome.sort-desc;size=24";

    /** the name of the small(default: 16x16 px) sort-up image (used for menuitems or buttons). */
    public static final String SORT_UP_SMALL = "FontAwesome.sort-up";
    /** the name of the large(default: 24x24 px) sort-up image (used for toolbar buttons or icons). */
    public static final String SORT_UP_LARGE = "FontAwesome.sort-up;size=24";

    /** the name of the small(default: 16x16 px) sort-asc image (used for menuitems or buttons). */
    public static final String SORT_ASC_SMALL = "FontAwesome.sort-asc";
    /** the name of the large(default: 24x24 px) sort-asc image (used for toolbar buttons or icons). */
    public static final String SORT_ASC_LARGE = "FontAwesome.sort-asc;size=24";

    /** the name of the small(default: 16x16 px) envelope image (used for menuitems or buttons). */
    public static final String ENVELOPE_SMALL = "FontAwesome.envelope";
    /** the name of the large(default: 24x24 px) envelope image (used for toolbar buttons or icons). */
    public static final String ENVELOPE_LARGE = "FontAwesome.envelope;size=24";

    /** the name of the small(default: 16x16 px) linkedin image (used for menuitems or buttons). */
    public static final String LINKEDIN_SMALL = "FontAwesome.linkedin";
    /** the name of the large(default: 24x24 px) linkedin image (used for toolbar buttons or icons). */
    public static final String LINKEDIN_LARGE = "FontAwesome.linkedin;size=24";

    /** the name of the small(default: 16x16 px) rotate-left image (used for menuitems or buttons). */
    public static final String ROTATE_LEFT_SMALL = "FontAwesome.rotate-left";
    /** the name of the large(default: 24x24 px) rotate-left image (used for toolbar buttons or icons). */
    public static final String ROTATE_LEFT_LARGE = "FontAwesome.rotate-left;size=24";

    /** the name of the small(default: 16x16 px) undo image (used for menuitems or buttons). */
    public static final String UNDO_SMALL = "FontAwesome.undo";
    /** the name of the large(default: 24x24 px) undo image (used for toolbar buttons or icons). */
    public static final String UNDO_LARGE = "FontAwesome.undo;size=24";

    /** the name of the small(default: 16x16 px) legal image (used for menuitems or buttons). */
    public static final String LEGAL_SMALL = "FontAwesome.legal";
    /** the name of the large(default: 24x24 px) legal image (used for toolbar buttons or icons). */
    public static final String LEGAL_LARGE = "FontAwesome.legal;size=24";

    /** the name of the small(default: 16x16 px) gavel image (used for menuitems or buttons). */
    public static final String GAVEL_SMALL = "FontAwesome.gavel";
    /** the name of the large(default: 24x24 px) gavel image (used for toolbar buttons or icons). */
    public static final String GAVEL_LARGE = "FontAwesome.gavel;size=24";

    /** the name of the small(default: 16x16 px) dashboard image (used for menuitems or buttons). */
    public static final String DASHBOARD_SMALL = "FontAwesome.dashboard";
    /** the name of the large(default: 24x24 px) dashboard image (used for toolbar buttons or icons). */
    public static final String DASHBOARD_LARGE = "FontAwesome.dashboard;size=24";

    /** the name of the small(default: 16x16 px) tachometer image (used for menuitems or buttons). */
    public static final String TACHOMETER_SMALL = "FontAwesome.tachometer";
    /** the name of the large(default: 24x24 px) tachometer image (used for toolbar buttons or icons). */
    public static final String TACHOMETER_LARGE = "FontAwesome.tachometer;size=24";

    /** the name of the small(default: 16x16 px) comment-o image (used for menuitems or buttons). */
    public static final String COMMENT_O_SMALL = "FontAwesome.comment-o";
    /** the name of the large(default: 24x24 px) comment-o image (used for toolbar buttons or icons). */
    public static final String COMMENT_O_LARGE = "FontAwesome.comment-o;size=24";

    /** the name of the small(default: 16x16 px) comments-o image (used for menuitems or buttons). */
    public static final String COMMENTS_O_SMALL = "FontAwesome.comments-o";
    /** the name of the large(default: 24x24 px) comments-o image (used for toolbar buttons or icons). */
    public static final String COMMENTS_O_LARGE = "FontAwesome.comments-o;size=24";

    /** the name of the small(default: 16x16 px) flash image (used for menuitems or buttons). */
    public static final String FLASH_SMALL = "FontAwesome.flash";
    /** the name of the large(default: 24x24 px) flash image (used for toolbar buttons or icons). */
    public static final String FLASH_LARGE = "FontAwesome.flash;size=24";

    /** the name of the small(default: 16x16 px) bolt image (used for menuitems or buttons). */
    public static final String BOLT_SMALL = "FontAwesome.bolt";
    /** the name of the large(default: 24x24 px) bolt image (used for toolbar buttons or icons). */
    public static final String BOLT_LARGE = "FontAwesome.bolt;size=24";

    /** the name of the small(default: 16x16 px) sitemap image (used for menuitems or buttons). */
    public static final String SITEMAP_SMALL = "FontAwesome.sitemap";
    /** the name of the large(default: 24x24 px) sitemap image (used for toolbar buttons or icons). */
    public static final String SITEMAP_LARGE = "FontAwesome.sitemap;size=24";

    /** the name of the small(default: 16x16 px) umbrella image (used for menuitems or buttons). */
    public static final String UMBRELLA_SMALL = "FontAwesome.umbrella";
    /** the name of the large(default: 24x24 px) umbrella image (used for toolbar buttons or icons). */
    public static final String UMBRELLA_LARGE = "FontAwesome.umbrella;size=24";

    /** the name of the small(default: 16x16 px) paste image (used for menuitems or buttons). */
    public static final String PASTE_SMALL = "FontAwesome.paste";
    /** the name of the large(default: 24x24 px) paste image (used for toolbar buttons or icons). */
    public static final String PASTE_LARGE = "FontAwesome.paste;size=24";

    /** the name of the small(default: 16x16 px) clipboard image (used for menuitems or buttons). */
    public static final String CLIPBOARD_SMALL = "FontAwesome.clipboard";
    /** the name of the large(default: 24x24 px) clipboard image (used for toolbar buttons or icons). */
    public static final String CLIPBOARD_LARGE = "FontAwesome.clipboard;size=24";

    /** the name of the small(default: 16x16 px) lightbulb-o image (used for menuitems or buttons). */
    public static final String LIGHTBULB_O_SMALL = "FontAwesome.lightbulb-o";
    /** the name of the large(default: 24x24 px) lightbulb-o image (used for toolbar buttons or icons). */
    public static final String LIGHTBULB_O_LARGE = "FontAwesome.lightbulb-o;size=24";

    /** the name of the small(default: 16x16 px) exchange image (used for menuitems or buttons). */
    public static final String EXCHANGE_SMALL = "FontAwesome.exchange";
    /** the name of the large(default: 24x24 px) exchange image (used for toolbar buttons or icons). */
    public static final String EXCHANGE_LARGE = "FontAwesome.exchange;size=24";

    /** the name of the small(default: 16x16 px) cloud-download image (used for menuitems or buttons). */
    public static final String CLOUD_DOWNLOAD_SMALL = "FontAwesome.cloud-download";
    /** the name of the large(default: 24x24 px) cloud-download image (used for toolbar buttons or icons). */
    public static final String CLOUD_DOWNLOAD_LARGE = "FontAwesome.cloud-download;size=24";

    /** the name of the small(default: 16x16 px) cloud-upload image (used for menuitems or buttons). */
    public static final String CLOUD_UPLOAD_SMALL = "FontAwesome.cloud-upload";
    /** the name of the large(default: 24x24 px) cloud-upload image (used for toolbar buttons or icons). */
    public static final String CLOUD_UPLOAD_LARGE = "FontAwesome.cloud-upload;size=24";

    /** the name of the small(default: 16x16 px) user-md image (used for menuitems or buttons). */
    public static final String USER_MD_SMALL = "FontAwesome.user-md";
    /** the name of the large(default: 24x24 px) user-md image (used for toolbar buttons or icons). */
    public static final String USER_MD_LARGE = "FontAwesome.user-md;size=24";

    /** the name of the small(default: 16x16 px) stethoscope image (used for menuitems or buttons). */
    public static final String STETHOSCOPE_SMALL = "FontAwesome.stethoscope";
    /** the name of the large(default: 24x24 px) stethoscope image (used for toolbar buttons or icons). */
    public static final String STETHOSCOPE_LARGE = "FontAwesome.stethoscope;size=24";

    /** the name of the small(default: 16x16 px) suitcase image (used for menuitems or buttons). */
    public static final String SUITCASE_SMALL = "FontAwesome.suitcase";
    /** the name of the large(default: 24x24 px) suitcase image (used for toolbar buttons or icons). */
    public static final String SUITCASE_LARGE = "FontAwesome.suitcase;size=24";

    /** the name of the small(default: 16x16 px) bell-o image (used for menuitems or buttons). */
    public static final String BELL_O_SMALL = "FontAwesome.bell-o";
    /** the name of the large(default: 24x24 px) bell-o image (used for toolbar buttons or icons). */
    public static final String BELL_O_LARGE = "FontAwesome.bell-o;size=24";

    /** the name of the small(default: 16x16 px) coffee image (used for menuitems or buttons). */
    public static final String COFFEE_SMALL = "FontAwesome.coffee";
    /** the name of the large(default: 24x24 px) coffee image (used for toolbar buttons or icons). */
    public static final String COFFEE_LARGE = "FontAwesome.coffee;size=24";

    /** the name of the small(default: 16x16 px) cutlery image (used for menuitems or buttons). */
    public static final String CUTLERY_SMALL = "FontAwesome.cutlery";
    /** the name of the large(default: 24x24 px) cutlery image (used for toolbar buttons or icons). */
    public static final String CUTLERY_LARGE = "FontAwesome.cutlery;size=24";

    /** the name of the small(default: 16x16 px) file-text-o image (used for menuitems or buttons). */
    public static final String FILE_TEXT_O_SMALL = "FontAwesome.file-text-o";
    /** the name of the large(default: 24x24 px) file-text-o image (used for toolbar buttons or icons). */
    public static final String FILE_TEXT_O_LARGE = "FontAwesome.file-text-o;size=24";

    /** the name of the small(default: 16x16 px) building-o image (used for menuitems or buttons). */
    public static final String BUILDING_O_SMALL = "FontAwesome.building-o";
    /** the name of the large(default: 24x24 px) building-o image (used for toolbar buttons or icons). */
    public static final String BUILDING_O_LARGE = "FontAwesome.building-o;size=24";

    /** the name of the small(default: 16x16 px) hospital-o image (used for menuitems or buttons). */
    public static final String HOSPITAL_O_SMALL = "FontAwesome.hospital-o";
    /** the name of the large(default: 24x24 px) hospital-o image (used for toolbar buttons or icons). */
    public static final String HOSPITAL_O_LARGE = "FontAwesome.hospital-o;size=24";

    /** the name of the small(default: 16x16 px) ambulance image (used for menuitems or buttons). */
    public static final String AMBULANCE_SMALL = "FontAwesome.ambulance";
    /** the name of the large(default: 24x24 px) ambulance image (used for toolbar buttons or icons). */
    public static final String AMBULANCE_LARGE = "FontAwesome.ambulance;size=24";

    /** the name of the small(default: 16x16 px) medkit image (used for menuitems or buttons). */
    public static final String MEDKIT_SMALL = "FontAwesome.medkit";
    /** the name of the large(default: 24x24 px) medkit image (used for toolbar buttons or icons). */
    public static final String MEDKIT_LARGE = "FontAwesome.medkit;size=24";

    /** the name of the small(default: 16x16 px) fighter-jet image (used for menuitems or buttons). */
    public static final String FIGHTER_JET_SMALL = "FontAwesome.fighter-jet";
    /** the name of the large(default: 24x24 px) fighter-jet image (used for toolbar buttons or icons). */
    public static final String FIGHTER_JET_LARGE = "FontAwesome.fighter-jet;size=24";

    /** the name of the small(default: 16x16 px) beer image (used for menuitems or buttons). */
    public static final String BEER_SMALL = "FontAwesome.beer";
    /** the name of the large(default: 24x24 px) beer image (used for toolbar buttons or icons). */
    public static final String BEER_LARGE = "FontAwesome.beer;size=24";

    /** the name of the small(default: 16x16 px) h-square image (used for menuitems or buttons). */
    public static final String H_SQUARE_SMALL = "FontAwesome.h-square";
    /** the name of the large(default: 24x24 px) h-square image (used for toolbar buttons or icons). */
    public static final String H_SQUARE_LARGE = "FontAwesome.h-square;size=24";

    /** the name of the small(default: 16x16 px) plus-square image (used for menuitems or buttons). */
    public static final String PLUS_SQUARE_SMALL = "FontAwesome.plus-square";
    /** the name of the large(default: 24x24 px) plus-square image (used for toolbar buttons or icons). */
    public static final String PLUS_SQUARE_LARGE = "FontAwesome.plus-square;size=24";

    /** the name of the small(default: 16x16 px) angle-double-left image (used for menuitems or buttons). */
    public static final String ANGLE_DOUBLE_LEFT_SMALL = "FontAwesome.angle-double-left";
    /** the name of the large(default: 24x24 px) angle-double-left image (used for toolbar buttons or icons). */
    public static final String ANGLE_DOUBLE_LEFT_LARGE = "FontAwesome.angle-double-left;size=24";

    /** the name of the small(default: 16x16 px) angle-double-right image (used for menuitems or buttons). */
    public static final String ANGLE_DOUBLE_RIGHT_SMALL = "FontAwesome.angle-double-right";
    /** the name of the large(default: 24x24 px) angle-double-right image (used for toolbar buttons or icons). */
    public static final String ANGLE_DOUBLE_RIGHT_LARGE = "FontAwesome.angle-double-right;size=24";

    /** the name of the small(default: 16x16 px) angle-double-up image (used for menuitems or buttons). */
    public static final String ANGLE_DOUBLE_UP_SMALL = "FontAwesome.angle-double-up";
    /** the name of the large(default: 24x24 px) angle-double-up image (used for toolbar buttons or icons). */
    public static final String ANGLE_DOUBLE_UP_LARGE = "FontAwesome.angle-double-up;size=24";

    /** the name of the small(default: 16x16 px) angle-double-down image (used for menuitems or buttons). */
    public static final String ANGLE_DOUBLE_DOWN_SMALL = "FontAwesome.angle-double-down";
    /** the name of the large(default: 24x24 px) angle-double-down image (used for toolbar buttons or icons). */
    public static final String ANGLE_DOUBLE_DOWN_LARGE = "FontAwesome.angle-double-down;size=24";

    /** the name of the small(default: 16x16 px) angle-left image (used for menuitems or buttons). */
    public static final String ANGLE_LEFT_SMALL = "FontAwesome.angle-left";
    /** the name of the large(default: 24x24 px) angle-left image (used for toolbar buttons or icons). */
    public static final String ANGLE_LEFT_LARGE = "FontAwesome.angle-left;size=24";

    /** the name of the small(default: 16x16 px) angle-right image (used for menuitems or buttons). */
    public static final String ANGLE_RIGHT_SMALL = "FontAwesome.angle-right";
    /** the name of the large(default: 24x24 px) angle-right image (used for toolbar buttons or icons). */
    public static final String ANGLE_RIGHT_LARGE = "FontAwesome.angle-right;size=24";

    /** the name of the small(default: 16x16 px) angle-up image (used for menuitems or buttons). */
    public static final String ANGLE_UP_SMALL = "FontAwesome.angle-up";
    /** the name of the large(default: 24x24 px) angle-up image (used for toolbar buttons or icons). */
    public static final String ANGLE_UP_LARGE = "FontAwesome.angle-up;size=24";

    /** the name of the small(default: 16x16 px) angle-down image (used for menuitems or buttons). */
    public static final String ANGLE_DOWN_SMALL = "FontAwesome.angle-down";
    /** the name of the large(default: 24x24 px) angle-down image (used for toolbar buttons or icons). */
    public static final String ANGLE_DOWN_LARGE = "FontAwesome.angle-down;size=24";

    /** the name of the small(default: 16x16 px) desktop image (used for menuitems or buttons). */
    public static final String DESKTOP_SMALL = "FontAwesome.desktop";
    /** the name of the large(default: 24x24 px) desktop image (used for toolbar buttons or icons). */
    public static final String DESKTOP_LARGE = "FontAwesome.desktop;size=24";

    /** the name of the small(default: 16x16 px) laptop image (used for menuitems or buttons). */
    public static final String LAPTOP_SMALL = "FontAwesome.laptop";
    /** the name of the large(default: 24x24 px) laptop image (used for toolbar buttons or icons). */
    public static final String LAPTOP_LARGE = "FontAwesome.laptop;size=24";

    /** the name of the small(default: 16x16 px) tablet image (used for menuitems or buttons). */
    public static final String TABLET_SMALL = "FontAwesome.tablet";
    /** the name of the large(default: 24x24 px) tablet image (used for toolbar buttons or icons). */
    public static final String TABLET_LARGE = "FontAwesome.tablet;size=24";

    /** the name of the small(default: 16x16 px) mobile-phone image (used for menuitems or buttons). */
    public static final String MOBILE_PHONE_SMALL = "FontAwesome.mobile-phone";
    /** the name of the large(default: 24x24 px) mobile-phone image (used for toolbar buttons or icons). */
    public static final String MOBILE_PHONE_LARGE = "FontAwesome.mobile-phone;size=24";

    /** the name of the small(default: 16x16 px) mobile image (used for menuitems or buttons). */
    public static final String MOBILE_SMALL = "FontAwesome.mobile";
    /** the name of the large(default: 24x24 px) mobile image (used for toolbar buttons or icons). */
    public static final String MOBILE_LARGE = "FontAwesome.mobile;size=24";

    /** the name of the small(default: 16x16 px) circle-o image (used for menuitems or buttons). */
    public static final String CIRCLE_O_SMALL = "FontAwesome.circle-o";
    /** the name of the large(default: 24x24 px) circle-o image (used for toolbar buttons or icons). */
    public static final String CIRCLE_O_LARGE = "FontAwesome.circle-o;size=24";

    /** the name of the small(default: 16x16 px) quote-left image (used for menuitems or buttons). */
    public static final String QUOTE_LEFT_SMALL = "FontAwesome.quote-left";
    /** the name of the large(default: 24x24 px) quote-left image (used for toolbar buttons or icons). */
    public static final String QUOTE_LEFT_LARGE = "FontAwesome.quote-left;size=24";

    /** the name of the small(default: 16x16 px) quote-right image (used for menuitems or buttons). */
    public static final String QUOTE_RIGHT_SMALL = "FontAwesome.quote-right";
    /** the name of the large(default: 24x24 px) quote-right image (used for toolbar buttons or icons). */
    public static final String QUOTE_RIGHT_LARGE = "FontAwesome.quote-right;size=24";

    /** the name of the small(default: 16x16 px) spinner image (used for menuitems or buttons). */
    public static final String SPINNER_SMALL = "FontAwesome.spinner";
    /** the name of the large(default: 24x24 px) spinner image (used for toolbar buttons or icons). */
    public static final String SPINNER_LARGE = "FontAwesome.spinner;size=24";

    /** the name of the small(default: 16x16 px) circle image (used for menuitems or buttons). */
    public static final String CIRCLE_SMALL = "FontAwesome.circle";
    /** the name of the large(default: 24x24 px) circle image (used for toolbar buttons or icons). */
    public static final String CIRCLE_LARGE = "FontAwesome.circle;size=24";

    /** the name of the small(default: 16x16 px) mail-reply image (used for menuitems or buttons). */
    public static final String MAIL_REPLY_SMALL = "FontAwesome.mail-reply";
    /** the name of the large(default: 24x24 px) mail-reply image (used for toolbar buttons or icons). */
    public static final String MAIL_REPLY_LARGE = "FontAwesome.mail-reply;size=24";

    /** the name of the small(default: 16x16 px) reply image (used for menuitems or buttons). */
    public static final String REPLY_SMALL = "FontAwesome.reply";
    /** the name of the large(default: 24x24 px) reply image (used for toolbar buttons or icons). */
    public static final String REPLY_LARGE = "FontAwesome.reply;size=24";

    /** the name of the small(default: 16x16 px) github-alt image (used for menuitems or buttons). */
    public static final String GITHUB_ALT_SMALL = "FontAwesome.github-alt";
    /** the name of the large(default: 24x24 px) github-alt image (used for toolbar buttons or icons). */
    public static final String GITHUB_ALT_LARGE = "FontAwesome.github-alt;size=24";

    /** the name of the small(default: 16x16 px) folder-o image (used for menuitems or buttons). */
    public static final String FOLDER_O_SMALL = "FontAwesome.folder-o";
    /** the name of the large(default: 24x24 px) folder-o image (used for toolbar buttons or icons). */
    public static final String FOLDER_O_LARGE = "FontAwesome.folder-o;size=24";

    /** the name of the small(default: 16x16 px) folder-open-o image (used for menuitems or buttons). */
    public static final String FOLDER_OPEN_O_SMALL = "FontAwesome.folder-open-o";
    /** the name of the large(default: 24x24 px) folder-open-o image (used for toolbar buttons or icons). */
    public static final String FOLDER_OPEN_O_LARGE = "FontAwesome.folder-open-o;size=24";

    /** the name of the small(default: 16x16 px) smile-o image (used for menuitems or buttons). */
    public static final String SMILE_O_SMALL = "FontAwesome.smile-o";
    /** the name of the large(default: 24x24 px) smile-o image (used for toolbar buttons or icons). */
    public static final String SMILE_O_LARGE = "FontAwesome.smile-o;size=24";

    /** the name of the small(default: 16x16 px) frown-o image (used for menuitems or buttons). */
    public static final String FROWN_O_SMALL = "FontAwesome.frown-o";
    /** the name of the large(default: 24x24 px) frown-o image (used for toolbar buttons or icons). */
    public static final String FROWN_O_LARGE = "FontAwesome.frown-o;size=24";

    /** the name of the small(default: 16x16 px) meh-o image (used for menuitems or buttons). */
    public static final String MEH_O_SMALL = "FontAwesome.meh-o";
    /** the name of the large(default: 24x24 px) meh-o image (used for toolbar buttons or icons). */
    public static final String MEH_O_LARGE = "FontAwesome.meh-o;size=24";

    /** the name of the small(default: 16x16 px) gamepad image (used for menuitems or buttons). */
    public static final String GAMEPAD_SMALL = "FontAwesome.gamepad";
    /** the name of the large(default: 24x24 px) gamepad image (used for toolbar buttons or icons). */
    public static final String GAMEPAD_LARGE = "FontAwesome.gamepad;size=24";

    /** the name of the small(default: 16x16 px) keyboard-o image (used for menuitems or buttons). */
    public static final String KEYBOARD_O_SMALL = "FontAwesome.keyboard-o";
    /** the name of the large(default: 24x24 px) keyboard-o image (used for toolbar buttons or icons). */
    public static final String KEYBOARD_O_LARGE = "FontAwesome.keyboard-o;size=24";

    /** the name of the small(default: 16x16 px) flag-o image (used for menuitems or buttons). */
    public static final String FLAG_O_SMALL = "FontAwesome.flag-o";
    /** the name of the large(default: 24x24 px) flag-o image (used for toolbar buttons or icons). */
    public static final String FLAG_O_LARGE = "FontAwesome.flag-o;size=24";

    /** the name of the small(default: 16x16 px) flag-checkered image (used for menuitems or buttons). */
    public static final String FLAG_CHECKERED_SMALL = "FontAwesome.flag-checkered";
    /** the name of the large(default: 24x24 px) flag-checkered image (used for toolbar buttons or icons). */
    public static final String FLAG_CHECKERED_LARGE = "FontAwesome.flag-checkered;size=24";

    /** the name of the small(default: 16x16 px) terminal image (used for menuitems or buttons). */
    public static final String TERMINAL_SMALL = "FontAwesome.terminal";
    /** the name of the large(default: 24x24 px) terminal image (used for toolbar buttons or icons). */
    public static final String TERMINAL_LARGE = "FontAwesome.terminal;size=24";

    /** the name of the small(default: 16x16 px) code image (used for menuitems or buttons). */
    public static final String CODE_SMALL = "FontAwesome.code";
    /** the name of the large(default: 24x24 px) code image (used for toolbar buttons or icons). */
    public static final String CODE_LARGE = "FontAwesome.code;size=24";

    /** the name of the small(default: 16x16 px) mail-reply-all image (used for menuitems or buttons). */
    public static final String MAIL_REPLY_ALL_SMALL = "FontAwesome.mail-reply-all";
    /** the name of the large(default: 24x24 px) mail-reply-all image (used for toolbar buttons or icons). */
    public static final String MAIL_REPLY_ALL_LARGE = "FontAwesome.mail-reply-all;size=24";

    /** the name of the small(default: 16x16 px) reply-all image (used for menuitems or buttons). */
    public static final String REPLY_ALL_SMALL = "FontAwesome.reply-all";
    /** the name of the large(default: 24x24 px) reply-all image (used for toolbar buttons or icons). */
    public static final String REPLY_ALL_LARGE = "FontAwesome.reply-all;size=24";

    /** the name of the small(default: 16x16 px) star-half-empty image (used for menuitems or buttons). */
    public static final String STAR_HALF_EMPTY_SMALL = "FontAwesome.star-half-empty";
    /** the name of the large(default: 24x24 px) star-half-empty image (used for toolbar buttons or icons). */
    public static final String STAR_HALF_EMPTY_LARGE = "FontAwesome.star-half-empty;size=24";

    /** the name of the small(default: 16x16 px) star-half-full image (used for menuitems or buttons). */
    public static final String STAR_HALF_FULL_SMALL = "FontAwesome.star-half-full";
    /** the name of the large(default: 24x24 px) star-half-full image (used for toolbar buttons or icons). */
    public static final String STAR_HALF_FULL_LARGE = "FontAwesome.star-half-full;size=24";

    /** the name of the small(default: 16x16 px) star-half-o image (used for menuitems or buttons). */
    public static final String STAR_HALF_O_SMALL = "FontAwesome.star-half-o";
    /** the name of the large(default: 24x24 px) star-half-o image (used for toolbar buttons or icons). */
    public static final String STAR_HALF_O_LARGE = "FontAwesome.star-half-o;size=24";

    /** the name of the small(default: 16x16 px) location-arrow image (used for menuitems or buttons). */
    public static final String LOCATION_ARROW_SMALL = "FontAwesome.location-arrow";
    /** the name of the large(default: 24x24 px) location-arrow image (used for toolbar buttons or icons). */
    public static final String LOCATION_ARROW_LARGE = "FontAwesome.location-arrow;size=24";

    /** the name of the small(default: 16x16 px) crop image (used for menuitems or buttons). */
    public static final String CROP_SMALL = "FontAwesome.crop";
    /** the name of the large(default: 24x24 px) crop image (used for toolbar buttons or icons). */
    public static final String CROP_LARGE = "FontAwesome.crop;size=24";

    /** the name of the small(default: 16x16 px) code-fork image (used for menuitems or buttons). */
    public static final String CODE_FORK_SMALL = "FontAwesome.code-fork";
    /** the name of the large(default: 24x24 px) code-fork image (used for toolbar buttons or icons). */
    public static final String CODE_FORK_LARGE = "FontAwesome.code-fork;size=24";

    /** the name of the small(default: 16x16 px) unlink image (used for menuitems or buttons). */
    public static final String UNLINK_SMALL = "FontAwesome.unlink";
    /** the name of the large(default: 24x24 px) unlink image (used for toolbar buttons or icons). */
    public static final String UNLINK_LARGE = "FontAwesome.unlink;size=24";

    /** the name of the small(default: 16x16 px) chain-broken image (used for menuitems or buttons). */
    public static final String CHAIN_BROKEN_SMALL = "FontAwesome.chain-broken";
    /** the name of the large(default: 24x24 px) chain-broken image (used for toolbar buttons or icons). */
    public static final String CHAIN_BROKEN_LARGE = "FontAwesome.chain-broken;size=24";

    /** the name of the small(default: 16x16 px) question image (used for menuitems or buttons). */
    public static final String QUESTION_SMALL = "FontAwesome.question";
    /** the name of the large(default: 24x24 px) question image (used for toolbar buttons or icons). */
    public static final String QUESTION_LARGE = "FontAwesome.question;size=24";

    /** the name of the small(default: 16x16 px) info image (used for menuitems or buttons). */
    public static final String INFO_SMALL = "FontAwesome.info";
    /** the name of the large(default: 24x24 px) info image (used for toolbar buttons or icons). */
    public static final String INFO_LARGE = "FontAwesome.info;size=24";

    /** the name of the small(default: 16x16 px) exclamation image (used for menuitems or buttons). */
    public static final String EXCLAMATION_SMALL = "FontAwesome.exclamation";
    /** the name of the large(default: 24x24 px) exclamation image (used for toolbar buttons or icons). */
    public static final String EXCLAMATION_LARGE = "FontAwesome.exclamation;size=24";

    /** the name of the small(default: 16x16 px) superscript image (used for menuitems or buttons). */
    public static final String SUPERSCRIPT_SMALL = "FontAwesome.superscript";
    /** the name of the large(default: 24x24 px) superscript image (used for toolbar buttons or icons). */
    public static final String SUPERSCRIPT_LARGE = "FontAwesome.superscript;size=24";

    /** the name of the small(default: 16x16 px) subscript image (used for menuitems or buttons). */
    public static final String SUBSCRIPT_SMALL = "FontAwesome.subscript";
    /** the name of the large(default: 24x24 px) subscript image (used for toolbar buttons or icons). */
    public static final String SUBSCRIPT_LARGE = "FontAwesome.subscript;size=24";

    /** the name of the small(default: 16x16 px) eraser image (used for menuitems or buttons). */
    public static final String ERASER_SMALL = "FontAwesome.eraser";
    /** the name of the large(default: 24x24 px) eraser image (used for toolbar buttons or icons). */
    public static final String ERASER_LARGE = "FontAwesome.eraser;size=24";

    /** the name of the small(default: 16x16 px) puzzle-piece image (used for menuitems or buttons). */
    public static final String PUZZLE_PIECE_SMALL = "FontAwesome.puzzle-piece";
    /** the name of the large(default: 24x24 px) puzzle-piece image (used for toolbar buttons or icons). */
    public static final String PUZZLE_PIECE_LARGE = "FontAwesome.puzzle-piece;size=24";

    /** the name of the small(default: 16x16 px) microphone image (used for menuitems or buttons). */
    public static final String MICROPHONE_SMALL = "FontAwesome.microphone";
    /** the name of the large(default: 24x24 px) microphone image (used for toolbar buttons or icons). */
    public static final String MICROPHONE_LARGE = "FontAwesome.microphone;size=24";

    /** the name of the small(default: 16x16 px) microphone-slash image (used for menuitems or buttons). */
    public static final String MICROPHONE_SLASH_SMALL = "FontAwesome.microphone-slash";
    /** the name of the large(default: 24x24 px) microphone-slash image (used for toolbar buttons or icons). */
    public static final String MICROPHONE_SLASH_LARGE = "FontAwesome.microphone-slash;size=24";

    /** the name of the small(default: 16x16 px) shield image (used for menuitems or buttons). */
    public static final String SHIELD_SMALL = "FontAwesome.shield";
    /** the name of the large(default: 24x24 px) shield image (used for toolbar buttons or icons). */
    public static final String SHIELD_LARGE = "FontAwesome.shield;size=24";

    /** the name of the small(default: 16x16 px) calendar-o image (used for menuitems or buttons). */
    public static final String CALENDAR_O_SMALL = "FontAwesome.calendar-o";
    /** the name of the large(default: 24x24 px) calendar-o image (used for toolbar buttons or icons). */
    public static final String CALENDAR_O_LARGE = "FontAwesome.calendar-o;size=24";

    /** the name of the small(default: 16x16 px) fire-extinguisher image (used for menuitems or buttons). */
    public static final String FIRE_EXTINGUISHER_SMALL = "FontAwesome.fire-extinguisher";
    /** the name of the large(default: 24x24 px) fire-extinguisher image (used for toolbar buttons or icons). */
    public static final String FIRE_EXTINGUISHER_LARGE = "FontAwesome.fire-extinguisher;size=24";

    /** the name of the small(default: 16x16 px) rocket image (used for menuitems or buttons). */
    public static final String ROCKET_SMALL = "FontAwesome.rocket";
    /** the name of the large(default: 24x24 px) rocket image (used for toolbar buttons or icons). */
    public static final String ROCKET_LARGE = "FontAwesome.rocket;size=24";

    /** the name of the small(default: 16x16 px) maxcdn image (used for menuitems or buttons). */
    public static final String MAXCDN_SMALL = "FontAwesome.maxcdn";
    /** the name of the large(default: 24x24 px) maxcdn image (used for toolbar buttons or icons). */
    public static final String MAXCDN_LARGE = "FontAwesome.maxcdn;size=24";

    /** the name of the small(default: 16x16 px) chevron-circle-left image (used for menuitems or buttons). */
    public static final String CHEVRON_CIRCLE_LEFT_SMALL = "FontAwesome.chevron-circle-left";
    /** the name of the large(default: 24x24 px) chevron-circle-left image (used for toolbar buttons or icons). */
    public static final String CHEVRON_CIRCLE_LEFT_LARGE = "FontAwesome.chevron-circle-left;size=24";

    /** the name of the small(default: 16x16 px) chevron-circle-right image (used for menuitems or buttons). */
    public static final String CHEVRON_CIRCLE_RIGHT_SMALL = "FontAwesome.chevron-circle-right";
    /** the name of the large(default: 24x24 px) chevron-circle-right image (used for toolbar buttons or icons). */
    public static final String CHEVRON_CIRCLE_RIGHT_LARGE = "FontAwesome.chevron-circle-right;size=24";

    /** the name of the small(default: 16x16 px) chevron-circle-up image (used for menuitems or buttons). */
    public static final String CHEVRON_CIRCLE_UP_SMALL = "FontAwesome.chevron-circle-up";
    /** the name of the large(default: 24x24 px) chevron-circle-up image (used for toolbar buttons or icons). */
    public static final String CHEVRON_CIRCLE_UP_LARGE = "FontAwesome.chevron-circle-up;size=24";

    /** the name of the small(default: 16x16 px) chevron-circle-down image (used for menuitems or buttons). */
    public static final String CHEVRON_CIRCLE_DOWN_SMALL = "FontAwesome.chevron-circle-down";
    /** the name of the large(default: 24x24 px) chevron-circle-down image (used for toolbar buttons or icons). */
    public static final String CHEVRON_CIRCLE_DOWN_LARGE = "FontAwesome.chevron-circle-down;size=24";

    /** the name of the small(default: 16x16 px) html5 image (used for menuitems or buttons). */
    public static final String HTML5_SMALL = "FontAwesome.html5";
    /** the name of the large(default: 24x24 px) html5 image (used for toolbar buttons or icons). */
    public static final String HTML5_LARGE = "FontAwesome.html5;size=24";

    /** the name of the small(default: 16x16 px) css3 image (used for menuitems or buttons). */
    public static final String CSS3_SMALL = "FontAwesome.css3";
    /** the name of the large(default: 24x24 px) css3 image (used for toolbar buttons or icons). */
    public static final String CSS3_LARGE = "FontAwesome.css3;size=24";

    /** the name of the small(default: 16x16 px) anchor image (used for menuitems or buttons). */
    public static final String ANCHOR_SMALL = "FontAwesome.anchor";
    /** the name of the large(default: 24x24 px) anchor image (used for toolbar buttons or icons). */
    public static final String ANCHOR_LARGE = "FontAwesome.anchor;size=24";

    /** the name of the small(default: 16x16 px) unlock-alt image (used for menuitems or buttons). */
    public static final String UNLOCK_ALT_SMALL = "FontAwesome.unlock-alt";
    /** the name of the large(default: 24x24 px) unlock-alt image (used for toolbar buttons or icons). */
    public static final String UNLOCK_ALT_LARGE = "FontAwesome.unlock-alt;size=24";

    /** the name of the small(default: 16x16 px) bullseye image (used for menuitems or buttons). */
    public static final String BULLSEYE_SMALL = "FontAwesome.bullseye";
    /** the name of the large(default: 24x24 px) bullseye image (used for toolbar buttons or icons). */
    public static final String BULLSEYE_LARGE = "FontAwesome.bullseye;size=24";

    /** the name of the small(default: 16x16 px) ellipsis-h image (used for menuitems or buttons). */
    public static final String ELLIPSIS_H_SMALL = "FontAwesome.ellipsis-h";
    /** the name of the large(default: 24x24 px) ellipsis-h image (used for toolbar buttons or icons). */
    public static final String ELLIPSIS_H_LARGE = "FontAwesome.ellipsis-h;size=24";

    /** the name of the small(default: 16x16 px) ellipsis-v image (used for menuitems or buttons). */
    public static final String ELLIPSIS_V_SMALL = "FontAwesome.ellipsis-v";
    /** the name of the large(default: 24x24 px) ellipsis-v image (used for toolbar buttons or icons). */
    public static final String ELLIPSIS_V_LARGE = "FontAwesome.ellipsis-v;size=24";

    /** the name of the small(default: 16x16 px) rss-square image (used for menuitems or buttons). */
    public static final String RSS_SQUARE_SMALL = "FontAwesome.rss-square";
    /** the name of the large(default: 24x24 px) rss-square image (used for toolbar buttons or icons). */
    public static final String RSS_SQUARE_LARGE = "FontAwesome.rss-square;size=24";

    /** the name of the small(default: 16x16 px) play-circle image (used for menuitems or buttons). */
    public static final String PLAY_CIRCLE_SMALL = "FontAwesome.play-circle";
    /** the name of the large(default: 24x24 px) play-circle image (used for toolbar buttons or icons). */
    public static final String PLAY_CIRCLE_LARGE = "FontAwesome.play-circle;size=24";

    /** the name of the small(default: 16x16 px) ticket image (used for menuitems or buttons). */
    public static final String TICKET_SMALL = "FontAwesome.ticket";
    /** the name of the large(default: 24x24 px) ticket image (used for toolbar buttons or icons). */
    public static final String TICKET_LARGE = "FontAwesome.ticket;size=24";

    /** the name of the small(default: 16x16 px) minus-square image (used for menuitems or buttons). */
    public static final String MINUS_SQUARE_SMALL = "FontAwesome.minus-square";
    /** the name of the large(default: 24x24 px) minus-square image (used for toolbar buttons or icons). */
    public static final String MINUS_SQUARE_LARGE = "FontAwesome.minus-square;size=24";

    /** the name of the small(default: 16x16 px) minus-square-o image (used for menuitems or buttons). */
    public static final String MINUS_SQUARE_O_SMALL = "FontAwesome.minus-square-o";
    /** the name of the large(default: 24x24 px) minus-square-o image (used for toolbar buttons or icons). */
    public static final String MINUS_SQUARE_O_LARGE = "FontAwesome.minus-square-o;size=24";

    /** the name of the small(default: 16x16 px) level-up image (used for menuitems or buttons). */
    public static final String LEVEL_UP_SMALL = "FontAwesome.level-up";
    /** the name of the large(default: 24x24 px) level-up image (used for toolbar buttons or icons). */
    public static final String LEVEL_UP_LARGE = "FontAwesome.level-up;size=24";

    /** the name of the small(default: 16x16 px) level-down image (used for menuitems or buttons). */
    public static final String LEVEL_DOWN_SMALL = "FontAwesome.level-down";
    /** the name of the large(default: 24x24 px) level-down image (used for toolbar buttons or icons). */
    public static final String LEVEL_DOWN_LARGE = "FontAwesome.level-down;size=24";

    /** the name of the small(default: 16x16 px) check-square image (used for menuitems or buttons). */
    public static final String CHECK_SQUARE_SMALL = "FontAwesome.check-square";
    /** the name of the large(default: 24x24 px) check-square image (used for toolbar buttons or icons). */
    public static final String CHECK_SQUARE_LARGE = "FontAwesome.check-square;size=24";

    /** the name of the small(default: 16x16 px) pencil-square image (used for menuitems or buttons). */
    public static final String PENCIL_SQUARE_SMALL = "FontAwesome.pencil-square";
    /** the name of the large(default: 24x24 px) pencil-square image (used for toolbar buttons or icons). */
    public static final String PENCIL_SQUARE_LARGE = "FontAwesome.pencil-square;size=24";

    /** the name of the small(default: 16x16 px) external-link-square image (used for menuitems or buttons). */
    public static final String EXTERNAL_LINK_SQUARE_SMALL = "FontAwesome.external-link-square";
    /** the name of the large(default: 24x24 px) external-link-square image (used for toolbar buttons or icons). */
    public static final String EXTERNAL_LINK_SQUARE_LARGE = "FontAwesome.external-link-square;size=24";

    /** the name of the small(default: 16x16 px) share-square image (used for menuitems or buttons). */
    public static final String SHARE_SQUARE_SMALL = "FontAwesome.share-square";
    /** the name of the large(default: 24x24 px) share-square image (used for toolbar buttons or icons). */
    public static final String SHARE_SQUARE_LARGE = "FontAwesome.share-square;size=24";

    /** the name of the small(default: 16x16 px) compass image (used for menuitems or buttons). */
    public static final String COMPASS_SMALL = "FontAwesome.compass";
    /** the name of the large(default: 24x24 px) compass image (used for toolbar buttons or icons). */
    public static final String COMPASS_LARGE = "FontAwesome.compass;size=24";

    /** the name of the small(default: 16x16 px) caret-square-o-down image (used for menuitems or buttons). */
    public static final String CARET_SQUARE_O_DOWN_SMALL = "FontAwesome.caret-square-o-down";
    /** the name of the large(default: 24x24 px) caret-square-o-down image (used for toolbar buttons or icons). */
    public static final String CARET_SQUARE_O_DOWN_LARGE = "FontAwesome.caret-square-o-down;size=24";

   /** the name of the small(default: 16x16 px) caret-square-o-up image (used for menuitems or buttons). */
    public static final String CARET_SQUARE_O_UP_SMALL = "FontAwesome.caret-square-o-up";
    /** the name of the large(default: 24x24 px) caret-square-o-up image (used for toolbar buttons or icons). */
    public static final String CARET_SQUARE_O_UP_LARGE = "FontAwesome.caret-square-o-up;size=24";

    /** the name of the small(default: 16x16 px) caret-square-o-right image (used for menuitems or buttons). */
    public static final String CARET_SQUARE_O_RIGHT_SMALL = "FontAwesome.caret-square-o-right";
    /** the name of the large(default: 24x24 px) caret-square-o-right image (used for toolbar buttons or icons). */
    public static final String CARET_SQUARE_O_RIGHT_LARGE = "FontAwesome.caret-square-o-right;size=24";

    /** the name of the small(default: 16x16 px) euro image (used for menuitems or buttons). */
    public static final String EURO_SMALL = "FontAwesome.euro";
    /** the name of the large(default: 24x24 px) euro image (used for toolbar buttons or icons). */
    public static final String EURO_LARGE = "FontAwesome.euro;size=24";

    /** the name of the small(default: 16x16 px) gbp image (used for menuitems or buttons). */
    public static final String GBP_SMALL = "FontAwesome.gbp";
    /** the name of the large(default: 24x24 px) gbp image (used for toolbar buttons or icons). */
    public static final String GBP_LARGE = "FontAwesome.gbp;size=24";

    /** the name of the small(default: 16x16 px) dollar image (used for menuitems or buttons). */
    public static final String DOLLAR_SMALL = "FontAwesome.dollar";
    /** the name of the large(default: 24x24 px) dollar image (used for toolbar buttons or icons). */
    public static final String DOLLAR_LARGE = "FontAwesome.dollar;size=24";

    /** the name of the small(default: 16x16 px) usd image (used for menuitems or buttons). */
    public static final String USD_SMALL = "FontAwesome.usd";
    /** the name of the large(default: 24x24 px) usd image (used for toolbar buttons or icons). */
    public static final String USD_LARGE = "FontAwesome.usd;size=24";

    /** the name of the small(default: 16x16 px) rupee image (used for menuitems or buttons). */
    public static final String RUPEE_SMALL = "FontAwesome.rupee";
    /** the name of the large(default: 24x24 px) rupee image (used for toolbar buttons or icons). */
    public static final String RUPEE_LARGE = "FontAwesome.rupee;size=24";

    /** the name of the small(default: 16x16 px) inr image (used for menuitems or buttons). */
    public static final String INR_SMALL = "FontAwesome.inr";
    /** the name of the large(default: 24x24 px) inr image (used for toolbar buttons or icons). */
    public static final String INR_LARGE = "FontAwesome.inr;size=24";

    /** the name of the small(default: 16x16 px) cny image (used for menuitems or buttons). */
    public static final String CNY_SMALL = "FontAwesome.cny";
    /** the name of the large(default: 24x24 px) cny image (used for toolbar buttons or icons). */
    public static final String CNY_LARGE = "FontAwesome.cny;size=24";

    /** the name of the small(default: 16x16 px) rmb image (used for menuitems or buttons). */
    public static final String RMB_SMALL = "FontAwesome.rmb";
    /** the name of the large(default: 24x24 px) rmb image (used for toolbar buttons or icons). */
    public static final String RMB_LARGE = "FontAwesome.rmb;size=24";

    /** the name of the small(default: 16x16 px) yen image (used for menuitems or buttons). */
    public static final String YEN_SMALL = "FontAwesome.yen";
    /** the name of the large(default: 24x24 px) yen image (used for toolbar buttons or icons). */
    public static final String YEN_LARGE = "FontAwesome.yen;size=24";

    /** the name of the small(default: 16x16 px) jpy image (used for menuitems or buttons). */
    public static final String JPY_SMALL = "FontAwesome.jpy";
    /** the name of the large(default: 24x24 px) jpy image (used for toolbar buttons or icons). */
    public static final String JPY_LARGE = "FontAwesome.jpy;size=24";

    /** the name of the small(default: 16x16 px) ruble image (used for menuitems or buttons). */
    public static final String RUBLE_SMALL = "FontAwesome.ruble";
    /** the name of the large(default: 24x24 px) ruble image (used for toolbar buttons or icons). */
    public static final String RUBLE_LARGE = "FontAwesome.ruble;size=24";

    /** the name of the small(default: 16x16 px) rouble image (used for menuitems or buttons). */
    public static final String ROUBLE_SMALL = "FontAwesome.rouble";
    /** the name of the large(default: 24x24 px) rouble image (used for toolbar buttons or icons). */
    public static final String ROUBLE_LARGE = "FontAwesome.rouble;size=24";

    /** the name of the small(default: 16x16 px) rub image (used for menuitems or buttons). */
    public static final String RUB_SMALL = "FontAwesome.rub";
    /** the name of the large(default: 24x24 px) rub image (used for toolbar buttons or icons). */
    public static final String RUB_LARGE = "FontAwesome.rub;size=24";

    /** the name of the small(default: 16x16 px) won image (used for menuitems or buttons). */
    public static final String WON_SMALL = "FontAwesome.won";
    /** the name of the large(default: 24x24 px) won image (used for toolbar buttons or icons). */
    public static final String WON_LARGE = "FontAwesome.won;size=24";

    /** the name of the small(default: 16x16 px) krw image (used for menuitems or buttons). */
    public static final String KRW_SMALL = "FontAwesome.krw";
    /** the name of the large(default: 24x24 px) krw image (used for toolbar buttons or icons). */
    public static final String KRW_LARGE = "FontAwesome.krw;size=24";

    /** the name of the small(default: 16x16 px) bitcoin image (used for menuitems or buttons). */
    public static final String BITCOIN_SMALL = "FontAwesome.bitcoin";
    /** the name of the large(default: 24x24 px) bitcoin image (used for toolbar buttons or icons). */
    public static final String BITCOIN_LARGE = "FontAwesome.bitcoin;size=24";

    /** the name of the small(default: 16x16 px) btc image (used for menuitems or buttons). */
    public static final String BTC_SMALL = "FontAwesome.btc";
    /** the name of the large(default: 24x24 px) btc image (used for toolbar buttons or icons). */
    public static final String BTC_LARGE = "FontAwesome.btc;size=24";

    /** the name of the small(default: 16x16 px) file image (used for menuitems or buttons). */
    public static final String FILE_SMALL = "FontAwesome.file";
    /** the name of the large(default: 24x24 px) file image (used for toolbar buttons or icons). */
    public static final String FILE_LARGE = "FontAwesome.file;size=24";

    /** the name of the small(default: 16x16 px) file-text image (used for menuitems or buttons). */
    public static final String FILE_TEXT_SMALL = "FontAwesome.file-text";
    /** the name of the large(default: 24x24 px) file-text image (used for toolbar buttons or icons). */
    public static final String FILE_TEXT_LARGE = "FontAwesome.file-text;size=24";

    /** the name of the small(default: 16x16 px) sort-alpha-asc image (used for menuitems or buttons). */
    public static final String SORT_ALPHA_ASC_SMALL = "FontAwesome.sort-alpha-asc";
    /** the name of the large(default: 24x24 px) sort-alpha-asc image (used for toolbar buttons or icons). */
    public static final String SORT_ALPHA_ASC_LARGE = "FontAwesome.sort-alpha-asc;size=24";

    /** the name of the small(default: 16x16 px) sort-alpha-desc image (used for menuitems or buttons). */
    public static final String SORT_ALPHA_DESC_SMALL = "FontAwesome.sort-alpha-desc";
    /** the name of the large(default: 24x24 px) sort-alpha-desc image (used for toolbar buttons or icons). */
    public static final String SORT_ALPHA_DESC_LARGE = "FontAwesome.sort-alpha-desc;size=24";

    /** the name of the small(default: 16x16 px) sort-amount-asc image (used for menuitems or buttons). */
    public static final String SORT_AMOUNT_ASC_SMALL = "FontAwesome.sort-amount-asc";
    /** the name of the large(default: 24x24 px) sort-amount-asc image (used for toolbar buttons or icons). */
    public static final String SORT_AMOUNT_ASC_LARGE = "FontAwesome.sort-amount-asc;size=24";

    /** the name of the small(default: 16x16 px) sort-amount-desc image (used for menuitems or buttons). */
    public static final String SORT_AMOUNT_DESC_SMALL = "FontAwesome.sort-amount-desc";
    /** the name of the large(default: 24x24 px) sort-amount-desc image (used for toolbar buttons or icons). */
    public static final String SORT_AMOUNT_DESC_LARGE = "FontAwesome.sort-amount-desc;size=24";

    /** the name of the small(default: 16x16 px) sort-numeric-asc image (used for menuitems or buttons). */
    public static final String SORT_NUMERIC_ASC_SMALL = "FontAwesome.sort-numeric-asc";
    /** the name of the large(default: 24x24 px) sort-numeric-asc image (used for toolbar buttons or icons). */
    public static final String SORT_NUMERIC_ASC_LARGE = "FontAwesome.sort-numeric-asc;size=24";

    /** the name of the small(default: 16x16 px) sort-numeric-desc image (used for menuitems or buttons). */
    public static final String SORT_NUMERIC_DESC_SMALL = "FontAwesome.sort-numeric-desc";
    /** the name of the large(default: 24x24 px) sort-numeric-desc image (used for toolbar buttons or icons). */
    public static final String SORT_NUMERIC_DESC_LARGE = "FontAwesome.sort-numeric-desc;size=24";

    /** the name of the small(default: 16x16 px) thumbs-up image (used for menuitems or buttons). */
    public static final String THUMBS_UP_SMALL = "FontAwesome.thumbs-up";
    /** the name of the large(default: 24x24 px) thumbs-up image (used for toolbar buttons or icons). */
    public static final String THUMBS_UP_LARGE = "FontAwesome.thumbs-up;size=24";

    /** the name of the small(default: 16x16 px) thumbs-down image (used for menuitems or buttons). */
    public static final String THUMBS_DOWN_SMALL = "FontAwesome.thumbs-down";
    /** the name of the large(default: 24x24 px) thumbs-down image (used for toolbar buttons or icons). */
    public static final String THUMBS_DOWN_LARGE = "FontAwesome.thumbs-down;size=24";

    /** the name of the small(default: 16x16 px) youtube-square image (used for menuitems or buttons). */
    public static final String YOUTUBE_SQUARE_SMALL = "FontAwesome.youtube-square";
    /** the name of the large(default: 24x24 px) youtube-square image (used for toolbar buttons or icons). */
    public static final String YOUTUBE_SQUARE_LARGE = "FontAwesome.youtube-square;size=24";

    /** the name of the small(default: 16x16 px) youtube image (used for menuitems or buttons). */
    public static final String YOUTUBE_SMALL = "FontAwesome.youtube";
    /** the name of the large(default: 24x24 px) youtube image (used for toolbar buttons or icons). */
    public static final String YOUTUBE_LARGE = "FontAwesome.youtube;size=24";

    /** the name of the small(default: 16x16 px) xing image (used for menuitems or buttons). */
    public static final String XING_SMALL = "FontAwesome.xing";
    /** the name of the large(default: 24x24 px) xing image (used for toolbar buttons or icons). */
    public static final String XING_LARGE = "FontAwesome.xing;size=24";

    /** the name of the small(default: 16x16 px) xing-square image (used for menuitems or buttons). */
    public static final String XING_SQUARE_SMALL = "FontAwesome.xing-square";
    /** the name of the large(default: 24x24 px) xing-square image (used for toolbar buttons or icons). */
    public static final String XING_SQUARE_LARGE = "FontAwesome.xing-square;size=24";

    /** the name of the small(default: 16x16 px) youtube-play image (used for menuitems or buttons). */
    public static final String YOUTUBE_PLAY_SMALL = "FontAwesome.youtube-play";
    /** the name of the large(default: 24x24 px) youtube-play image (used for toolbar buttons or icons). */
    public static final String YOUTUBE_PLAY_LARGE = "FontAwesome.youtube-play;size=24";

    /** the name of the small(default: 16x16 px) dropbox image (used for menuitems or buttons). */
    public static final String DROPBOX_SMALL = "FontAwesome.dropbox";
    /** the name of the large(default: 24x24 px) dropbox image (used for toolbar buttons or icons). */
    public static final String DROPBOX_LARGE = "FontAwesome.dropbox;size=24";

    /** the name of the small(default: 16x16 px) stack-overflow image (used for menuitems or buttons). */
    public static final String STACK_OVERFLOW_SMALL = "FontAwesome.stack-overflow";
    /** the name of the large(default: 24x24 px) stack-overflow image (used for toolbar buttons or icons). */
    public static final String STACK_OVERFLOW_LARGE = "FontAwesome.stack-overflow;size=24";

    /** the name of the small(default: 16x16 px) instagram image (used for menuitems or buttons). */
    public static final String INSTAGRAM_SMALL = "FontAwesome.instagram";
    /** the name of the large(default: 24x24 px) instagram image (used for toolbar buttons or icons). */
    public static final String INSTAGRAM_LARGE = "FontAwesome.instagram;size=24";

    /** the name of the small(default: 16x16 px) flickr image (used for menuitems or buttons). */
    public static final String FLICKR_SMALL = "FontAwesome.flickr";
    /** the name of the large(default: 24x24 px) flickr image (used for toolbar buttons or icons). */
    public static final String FLICKR_LARGE = "FontAwesome.flickr;size=24";

    /** the name of the small(default: 16x16 px) adn image (used for menuitems or buttons). */
    public static final String ADN_SMALL = "FontAwesome.adn";
    /** the name of the large(default: 24x24 px) adn image (used for toolbar buttons or icons). */
    public static final String ADN_LARGE = "FontAwesome.adn;size=24";

    /** the name of the small(default: 16x16 px) bitbucket image (used for menuitems or buttons). */
    public static final String BITBUCKET_SMALL = "FontAwesome.bitbucket";
    /** the name of the large(default: 24x24 px) bitbucket image (used for toolbar buttons or icons). */
    public static final String BITBUCKET_LARGE = "FontAwesome.bitbucket;size=24";

    /** the name of the small(default: 16x16 px) bitbucket-square image (used for menuitems or buttons). */
    public static final String BITBUCKET_SQUARE_SMALL = "FontAwesome.bitbucket-square";
    /** the name of the large(default: 24x24 px) bitbucket-square image (used for toolbar buttons or icons). */
    public static final String BITBUCKET_SQUARE_LARGE = "FontAwesome.bitbucket-square;size=24";

    /** the name of the small(default: 16x16 px) tumblr image (used for menuitems or buttons). */
    public static final String TUMBLR_SMALL = "FontAwesome.tumblr";
    /** the name of the large(default: 24x24 px) tumblr image (used for toolbar buttons or icons). */
    public static final String TUMBLR_LARGE = "FontAwesome.tumblr;size=24";

    /** the name of the small(default: 16x16 px) tumblr-square image (used for menuitems or buttons). */
    public static final String TUMBLR_SQUARE_SMALL = "FontAwesome.tumblr-square";
    /** the name of the large(default: 24x24 px) tumblr-square image (used for toolbar buttons or icons). */
    public static final String TUMBLR_SQUARE_LARGE = "FontAwesome.tumblr-square;size=24";

    /** the name of the small(default: 16x16 px) long-arrow-down image (used for menuitems or buttons). */
    public static final String LONG_ARROW_DOWN_SMALL = "FontAwesome.long-arrow-down";
    /** the name of the large(default: 24x24 px) long-arrow-down image (used for toolbar buttons or icons). */
    public static final String LONG_ARROW_DOWN_LARGE = "FontAwesome.long-arrow-down;size=24";

    /** the name of the small(default: 16x16 px) long-arrow-up image (used for menuitems or buttons). */
    public static final String LONG_ARROW_UP_SMALL = "FontAwesome.long-arrow-up";
    /** the name of the large(default: 24x24 px) long-arrow-up image (used for toolbar buttons or icons). */
    public static final String LONG_ARROW_UP_LARGE = "FontAwesome.long-arrow-up;size=24";

    /** the name of the small(default: 16x16 px) long-arrow-left image (used for menuitems or buttons). */
    public static final String LONG_ARROW_LEFT_SMALL = "FontAwesome.long-arrow-left";
    /** the name of the large(default: 24x24 px) long-arrow-left image (used for toolbar buttons or icons). */
    public static final String LONG_ARROW_LEFT_LARGE = "FontAwesome.long-arrow-left;size=24";

    /** the name of the small(default: 16x16 px) long-arrow-right image (used for menuitems or buttons). */
    public static final String LONG_ARROW_RIGHT_SMALL = "FontAwesome.long-arrow-right";
    /** the name of the large(default: 24x24 px) long-arrow-right image (used for toolbar buttons or icons). */
    public static final String LONG_ARROW_RIGHT_LARGE = "FontAwesome.long-arrow-right;size=24";

    /** the name of the small(default: 16x16 px) apple image (used for menuitems or buttons). */
    public static final String APPLE_SMALL = "FontAwesome.apple";
    /** the name of the large(default: 24x24 px) apple image (used for toolbar buttons or icons). */
    public static final String APPLE_LARGE = "FontAwesome.apple;size=24";

    /** the name of the small(default: 16x16 px) windows image (used for menuitems or buttons). */
    public static final String WINDOWS_SMALL = "FontAwesome.windows";
    /** the name of the large(default: 24x24 px) windows image (used for toolbar buttons or icons). */
    public static final String WINDOWS_LARGE = "FontAwesome.windows;size=24";

    /** the name of the small(default: 16x16 px) android image (used for menuitems or buttons). */
    public static final String ANDROID_SMALL = "FontAwesome.android";
    /** the name of the large(default: 24x24 px) android image (used for toolbar buttons or icons). */
    public static final String ANDROID_LARGE = "FontAwesome.android;size=24";

    /** the name of the small(default: 16x16 px) linux image (used for menuitems or buttons). */
    public static final String LINUX_SMALL = "FontAwesome.linux";
    /** the name of the large(default: 24x24 px) linux image (used for toolbar buttons or icons). */
    public static final String LINUX_LARGE = "FontAwesome.linux;size=24";

    /** the name of the small(default: 16x16 px) dribbble image (used for menuitems or buttons). */
    public static final String DRIBBBLE_SMALL = "FontAwesome.dribbble";
    /** the name of the large(default: 24x24 px) dribbble image (used for toolbar buttons or icons). */
    public static final String DRIBBBLE_LARGE = "FontAwesome.dribbble;size=24";

    /** the name of the small(default: 16x16 px) skype image (used for menuitems or buttons). */
    public static final String SKYPE_SMALL = "FontAwesome.skype";
    /** the name of the large(default: 24x24 px) skype image (used for toolbar buttons or icons). */
    public static final String SKYPE_LARGE = "FontAwesome.skype;size=24";

    /** the name of the small(default: 16x16 px) foursquare image (used for menuitems or buttons). */
    public static final String FOURSQUARE_SMALL = "FontAwesome.foursquare";
    /** the name of the large(default: 24x24 px) foursquare image (used for toolbar buttons or icons). */
    public static final String FOURSQUARE_LARGE = "FontAwesome.foursquare;size=24";

    /** the name of the small(default: 16x16 px) trello image (used for menuitems or buttons). */
    public static final String TRELLO_SMALL = "FontAwesome.trello";
    /** the name of the large(default: 24x24 px) trello image (used for toolbar buttons or icons). */
    public static final String TRELLO_LARGE = "FontAwesome.trello;size=24";

    /** the name of the small(default: 16x16 px) female image (used for menuitems or buttons). */
    public static final String FEMALE_SMALL = "FontAwesome.female";
    /** the name of the large(default: 24x24 px) female image (used for toolbar buttons or icons). */
    public static final String FEMALE_LARGE = "FontAwesome.female;size=24";

    /** the name of the small(default: 16x16 px) male image (used for menuitems or buttons). */
    public static final String MALE_SMALL = "FontAwesome.male";
    /** the name of the large(default: 24x24 px) male image (used for toolbar buttons or icons). */
    public static final String MALE_LARGE = "FontAwesome.male;size=24";

    /** the name of the small(default: 16x16 px) gittip image (used for menuitems or buttons). */
    public static final String GITTIP_SMALL = "FontAwesome.gittip";
    /** the name of the large(default: 24x24 px) gittip image (used for toolbar buttons or icons). */
    public static final String GITTIP_LARGE = "FontAwesome.gittip;size=24";

    /** the name of the small(default: 16x16 px) gratipay image (used for menuitems or buttons). */
    public static final String GRATIPAY_SMALL = "FontAwesome.gratipay";
    /** the name of the large(default: 24x24 px) gratipay image (used for toolbar buttons or icons). */
    public static final String GRATIPAY_LARGE = "FontAwesome.gratipay;size=24";

    /** the name of the small(default: 16x16 px) sun-o image (used for menuitems or buttons). */
    public static final String SUN_O_SMALL = "FontAwesome.sun-o";
    /** the name of the large(default: 24x24 px) sun-o image (used for toolbar buttons or icons). */
    public static final String SUN_O_LARGE = "FontAwesome.sun-o;size=24";

    /** the name of the small(default: 16x16 px) moon-o image (used for menuitems or buttons). */
    public static final String MOON_O_SMALL = "FontAwesome.moon-o";
    /** the name of the large(default: 24x24 px) moon-o image (used for toolbar buttons or icons). */
    public static final String MOON_O_LARGE = "FontAwesome.moon-o;size=24";

    /** the name of the small(default: 16x16 px) archive image (used for menuitems or buttons). */
    public static final String ARCHIVE_SMALL = "FontAwesome.archive";
    /** the name of the large(default: 24x24 px) archive image (used for toolbar buttons or icons). */
    public static final String ARCHIVE_LARGE = "FontAwesome.archive;size=24";

    /** the name of the small(default: 16x16 px) bug image (used for menuitems or buttons). */
    public static final String BUG_SMALL = "FontAwesome.bug";
    /** the name of the large(default: 24x24 px) bug image (used for toolbar buttons or icons). */
    public static final String BUG_LARGE = "FontAwesome.bug;size=24";

    /** the name of the small(default: 16x16 px) vk image (used for menuitems or buttons). */
    public static final String VK_SMALL = "FontAwesome.vk";
    /** the name of the large(default: 24x24 px) vk image (used for toolbar buttons or icons). */
    public static final String VK_LARGE = "FontAwesome.vk;size=24";

    /** the name of the small(default: 16x16 px) weibo image (used for menuitems or buttons). */
    public static final String WEIBO_SMALL = "FontAwesome.weibo";
    /** the name of the large(default: 24x24 px) weibo image (used for toolbar buttons or icons). */
    public static final String WEIBO_LARGE = "FontAwesome.weibo;size=24";

    /** the name of the small(default: 16x16 px) renren image (used for menuitems or buttons). */
    public static final String RENREN_SMALL = "FontAwesome.renren";
    /** the name of the large(default: 24x24 px) renren image (used for toolbar buttons or icons). */
    public static final String RENREN_LARGE = "FontAwesome.renren;size=24";

    /** the name of the small(default: 16x16 px) pagelines image (used for menuitems or buttons). */
    public static final String PAGELINES_SMALL = "FontAwesome.pagelines";
    /** the name of the large(default: 24x24 px) pagelines image (used for toolbar buttons or icons). */
    public static final String PAGELINES_LARGE = "FontAwesome.pagelines;size=24";

    /** the name of the small(default: 16x16 px) stack-exchange image (used for menuitems or buttons). */
    public static final String STACK_EXCHANGE_SMALL = "FontAwesome.stack-exchange";
    /** the name of the large(default: 24x24 px) stack-exchange image (used for toolbar buttons or icons). */
    public static final String STACK_EXCHANGE_LARGE = "FontAwesome.stack-exchange;size=24";

    /** the name of the small(default: 16x16 px) arrow-circle-o-right image (used for menuitems or buttons). */
    public static final String ARROW_CIRCLE_O_RIGHT_SMALL = "FontAwesome.arrow-circle-o-right";
    /** the name of the large(default: 24x24 px) arrow-circle-o-right image (used for toolbar buttons or icons). */
    public static final String ARROW_CIRCLE_O_RIGHT_LARGE = "FontAwesome.arrow-circle-o-right;size=24";

    /** the name of the small(default: 16x16 px) arrow-circle-o-left image (used for menuitems or buttons). */
    public static final String ARROW_CIRCLE_O_LEFT_SMALL = "FontAwesome.arrow-circle-o-left";
    /** the name of the large(default: 24x24 px) arrow-circle-o-left image (used for toolbar buttons or icons). */
    public static final String ARROW_CIRCLE_O_LEFT_LARGE = "FontAwesome.arrow-circle-o-left;size=24";

    /** the name of the small(default: 16x16 px) caret-square-o-left image (used for menuitems or buttons). */
    public static final String CARET_SQUARE_O_LEFT_SMALL = "FontAwesome.caret-square-o-left";
    /** the name of the large(default: 24x24 px) caret-square-o-left image (used for toolbar buttons or icons). */
    public static final String CARET_SQUARE_O_LEFT_LARGE = "FontAwesome.caret-square-o-left;size=24";

    /** the name of the small(default: 16x16 px) dot-circle-o image (used for menuitems or buttons). */
    public static final String DOT_CIRCLE_O_SMALL = "FontAwesome.dot-circle-o";
    /** the name of the large(default: 24x24 px) dot-circle-o image (used for toolbar buttons or icons). */
    public static final String DOT_CIRCLE_O_LARGE = "FontAwesome.dot-circle-o;size=24";

    /** the name of the small(default: 16x16 px) wheelchair image (used for menuitems or buttons). */
    public static final String WHEELCHAIR_SMALL = "FontAwesome.wheelchair";
    /** the name of the large(default: 24x24 px) wheelchair image (used for toolbar buttons or icons). */
    public static final String WHEELCHAIR_LARGE = "FontAwesome.wheelchair;size=24";

    /** the name of the small(default: 16x16 px) vimeo-square image (used for menuitems or buttons). */
    public static final String VIMEO_SQUARE_SMALL = "FontAwesome.vimeo-square";
    /** the name of the large(default: 24x24 px) vimeo-square image (used for toolbar buttons or icons). */
    public static final String VIMEO_SQUARE_LARGE = "FontAwesome.vimeo-square;size=24";

    /** the name of the small(default: 16x16 px) turkish-lira image (used for menuitems or buttons). */
    public static final String TURKISH_LIRA_SMALL = "FontAwesome.turkish-lira";
    /** the name of the large(default: 24x24 px) turkish-lira image (used for toolbar buttons or icons). */
    public static final String TURKISH_LIRA_LARGE = "FontAwesome.turkish-lira;size=24";

    /** the name of the small(default: 16x16 px) try image (used for menuitems or buttons). */
    public static final String TRY_SMALL = "FontAwesome.try";
    /** the name of the large(default: 24x24 px) try image (used for toolbar buttons or icons). */
    public static final String TRY_LARGE = "FontAwesome.try;size=24";

    /** the name of the small(default: 16x16 px) plus-square-o image (used for menuitems or buttons). */
    public static final String PLUS_SQUARE_O_SMALL = "FontAwesome.plus-square-o";
    /** the name of the large(default: 24x24 px) plus-square-o image (used for toolbar buttons or icons). */
    public static final String PLUS_SQUARE_O_LARGE = "FontAwesome.plus-square-o;size=24";

    /** the name of the small(default: 16x16 px) space-shuttle image (used for menuitems or buttons). */
    public static final String SPACE_SHUTTLE_SMALL = "FontAwesome.space-shuttle";
    /** the name of the large(default: 24x24 px) space-shuttle image (used for toolbar buttons or icons). */
    public static final String SPACE_SHUTTLE_LARGE = "FontAwesome.space-shuttle;size=24";

    /** the name of the small(default: 16x16 px) slack image (used for menuitems or buttons). */
    public static final String SLACK_SMALL = "FontAwesome.slack";
    /** the name of the large(default: 24x24 px) slack image (used for toolbar buttons or icons). */
    public static final String SLACK_LARGE = "FontAwesome.slack;size=24";

    /** the name of the small(default: 16x16 px) envelope-square image (used for menuitems or buttons). */
    public static final String ENVELOPE_SQUARE_SMALL = "FontAwesome.envelope-square";
    /** the name of the large(default: 24x24 px) envelope-square image (used for toolbar buttons or icons). */
    public static final String ENVELOPE_SQUARE_LARGE = "FontAwesome.envelope-square;size=24";

    /** the name of the small(default: 16x16 px) wordpress image (used for menuitems or buttons). */
    public static final String WORDPRESS_SMALL = "FontAwesome.wordpress";
    /** the name of the large(default: 24x24 px) wordpress image (used for toolbar buttons or icons). */
    public static final String WORDPRESS_LARGE = "FontAwesome.wordpress;size=24";

    /** the name of the small(default: 16x16 px) openid image (used for menuitems or buttons). */
    public static final String OPENID_SMALL = "FontAwesome.openid";
    /** the name of the large(default: 24x24 px) openid image (used for toolbar buttons or icons). */
    public static final String OPENID_LARGE = "FontAwesome.openid;size=24";

    /** the name of the small(default: 16x16 px) institution image (used for menuitems or buttons). */
    public static final String INSTITUTION_SMALL = "FontAwesome.institution";
    /** the name of the large(default: 24x24 px) institution image (used for toolbar buttons or icons). */
    public static final String INSTITUTION_LARGE = "FontAwesome.institution;size=24";

    /** the name of the small(default: 16x16 px) bank image (used for menuitems or buttons). */
    public static final String BANK_SMALL = "FontAwesome.bank";
    /** the name of the large(default: 24x24 px) bank image (used for toolbar buttons or icons). */
    public static final String BANK_LARGE = "FontAwesome.bank;size=24";

    /** the name of the small(default: 16x16 px) university image (used for menuitems or buttons). */
    public static final String UNIVERSITY_SMALL = "FontAwesome.university";
    /** the name of the large(default: 24x24 px) university image (used for toolbar buttons or icons). */
    public static final String UNIVERSITY_LARGE = "FontAwesome.university;size=24";

    /** the name of the small(default: 16x16 px) mortar-board image (used for menuitems or buttons). */
    public static final String MORTAR_BOARD_SMALL = "FontAwesome.mortar-board";
    /** the name of the large(default: 24x24 px) mortar-board image (used for toolbar buttons or icons). */
    public static final String MORTAR_BOARD_LARGE = "FontAwesome.mortar-board;size=24";

    /** the name of the small(default: 16x16 px) graduation-cap image (used for menuitems or buttons). */
    public static final String GRADUATION_CAP_SMALL = "FontAwesome.graduation-cap";
    /** the name of the large(default: 24x24 px) graduation-cap image (used for toolbar buttons or icons). */
    public static final String GRADUATION_CAP_LARGE = "FontAwesome.graduation-cap;size=24";

    /** the name of the small(default: 16x16 px) yahoo image (used for menuitems or buttons). */
    public static final String YAHOO_SMALL = "FontAwesome.yahoo";
    /** the name of the large(default: 24x24 px) yahoo image (used for toolbar buttons or icons). */
    public static final String YAHOO_LARGE = "FontAwesome.yahoo;size=24";

    /** the name of the small(default: 16x16 px) google image (used for menuitems or buttons). */
    public static final String GOOGLE_SMALL = "FontAwesome.google";
    /** the name of the large(default: 24x24 px) google image (used for toolbar buttons or icons). */
    public static final String GOOGLE_LARGE = "FontAwesome.google;size=24";

    /** the name of the small(default: 16x16 px) reddit image (used for menuitems or buttons). */
    public static final String REDDIT_SMALL = "FontAwesome.reddit";
    /** the name of the large(default: 24x24 px) reddit image (used for toolbar buttons or icons). */
    public static final String REDDIT_LARGE = "FontAwesome.reddit;size=24";

    /** the name of the small(default: 16x16 px) reddit-square image (used for menuitems or buttons). */
    public static final String REDDIT_SQUARE_SMALL = "FontAwesome.reddit-square";
    /** the name of the large(default: 24x24 px) reddit-square image (used for toolbar buttons or icons). */
    public static final String REDDIT_SQUARE_LARGE = "FontAwesome.reddit-square;size=24";

    /** the name of the small(default: 16x16 px) stumbleupon-circle image (used for menuitems or buttons). */
    public static final String STUMBLEUPON_CIRCLE_SMALL = "FontAwesome.stumbleupon-circle";
    /** the name of the large(default: 24x24 px) stumbleupon-circle image (used for toolbar buttons or icons). */
    public static final String STUMBLEUPON_CIRCLE_LARGE = "FontAwesome.stumbleupon-circle;size=24";

    /** the name of the small(default: 16x16 px) stumbleupon image (used for menuitems or buttons). */
    public static final String STUMBLEUPON_SMALL = "FontAwesome.stumbleupon";
    /** the name of the large(default: 24x24 px) stumbleupon image (used for toolbar buttons or icons). */
    public static final String STUMBLEUPON_LARGE = "FontAwesome.stumbleupon;size=24";

    /** the name of the small(default: 16x16 px) delicious image (used for menuitems or buttons). */
    public static final String DELICIOUS_SMALL = "FontAwesome.delicious";
    /** the name of the large(default: 24x24 px) delicious image (used for toolbar buttons or icons). */
    public static final String DELICIOUS_LARGE = "FontAwesome.delicious;size=24";

    /** the name of the small(default: 16x16 px) digg image (used for menuitems or buttons). */
    public static final String DIGG_SMALL = "FontAwesome.digg";
    /** the name of the large(default: 24x24 px) digg image (used for toolbar buttons or icons). */
    public static final String DIGG_LARGE = "FontAwesome.digg;size=24";

    /** the name of the small(default: 16x16 px) pied-piper image (used for menuitems or buttons). */
    public static final String PIED_PIPER_SMALL = "FontAwesome.pied-piper";
    /** the name of the large(default: 24x24 px) pied-piper image (used for toolbar buttons or icons). */
    public static final String PIED_PIPER_LARGE = "FontAwesome.pied-piper;size=24";

    /** the name of the small(default: 16x16 px) pied-piper-alt image (used for menuitems or buttons). */
    public static final String PIED_PIPER_ALT_SMALL = "FontAwesome.pied-piper-alt";
    /** the name of the large(default: 24x24 px) pied-piper-alt image (used for toolbar buttons or icons). */
    public static final String PIED_PIPER_ALT_LARGE = "FontAwesome.pied-piper-alt;size=24";

    /** the name of the small(default: 16x16 px) drupal image (used for menuitems or buttons). */
    public static final String DRUPAL_SMALL = "FontAwesome.drupal";
    /** the name of the large(default: 24x24 px) drupal image (used for toolbar buttons or icons). */
    public static final String DRUPAL_LARGE = "FontAwesome.drupal;size=24";

    /** the name of the small(default: 16x16 px) joomla image (used for menuitems or buttons). */
    public static final String JOOMLA_SMALL = "FontAwesome.joomla";
    /** the name of the large(default: 24x24 px) joomla image (used for toolbar buttons or icons). */
    public static final String JOOMLA_LARGE = "FontAwesome.joomla;size=24";

    /** the name of the small(default: 16x16 px) language image (used for menuitems or buttons). */
    public static final String LANGUAGE_SMALL = "FontAwesome.language";
    /** the name of the large(default: 24x24 px) language image (used for toolbar buttons or icons). */
    public static final String LANGUAGE_LARGE = "FontAwesome.language;size=24";

    /** the name of the small(default: 16x16 px) fax image (used for menuitems or buttons). */
    public static final String FAX_SMALL = "FontAwesome.fax";
    /** the name of the large(default: 24x24 px) fax image (used for toolbar buttons or icons). */
    public static final String FAX_LARGE = "FontAwesome.fax;size=24";

    /** the name of the small(default: 16x16 px) building image (used for menuitems or buttons). */
    public static final String BUILDING_SMALL = "FontAwesome.building";
    /** the name of the large(default: 24x24 px) building image (used for toolbar buttons or icons). */
    public static final String BUILDING_LARGE = "FontAwesome.building;size=24";

    /** the name of the small(default: 16x16 px) child image (used for menuitems or buttons). */
    public static final String CHILD_SMALL = "FontAwesome.child";
    /** the name of the large(default: 24x24 px) child image (used for toolbar buttons or icons). */
    public static final String CHILD_LARGE = "FontAwesome.child;size=24";

    /** the name of the small(default: 16x16 px) paw image (used for menuitems or buttons). */
    public static final String PAW_SMALL = "FontAwesome.paw";
    /** the name of the large(default: 24x24 px) paw image (used for toolbar buttons or icons). */
    public static final String PAW_LARGE = "FontAwesome.paw;size=24";

    /** the name of the small(default: 16x16 px) spoon image (used for menuitems or buttons). */
    public static final String SPOON_SMALL = "FontAwesome.spoon";
    /** the name of the large(default: 24x24 px) spoon image (used for toolbar buttons or icons). */
    public static final String SPOON_LARGE = "FontAwesome.spoon;size=24";

    /** the name of the small(default: 16x16 px) cube image (used for menuitems or buttons). */
    public static final String CUBE_SMALL = "FontAwesome.cube";
    /** the name of the large(default: 24x24 px) cube image (used for toolbar buttons or icons). */
    public static final String CUBE_LARGE = "FontAwesome.cube;size=24";

    /** the name of the small(default: 16x16 px) cubes image (used for menuitems or buttons). */
    public static final String CUBES_SMALL = "FontAwesome.cubes";
    /** the name of the large(default: 24x24 px) cubes image (used for toolbar buttons or icons). */
    public static final String CUBES_LARGE = "FontAwesome.cubes;size=24";

    /** the name of the small(default: 16x16 px) behance image (used for menuitems or buttons). */
    public static final String BEHANCE_SMALL = "FontAwesome.behance";
    /** the name of the large(default: 24x24 px) behance image (used for toolbar buttons or icons). */
    public static final String BEHANCE_LARGE = "FontAwesome.behance;size=24";

    /** the name of the small(default: 16x16 px) behance-square image (used for menuitems or buttons). */
    public static final String BEHANCE_SQUARE_SMALL = "FontAwesome.behance-square";
    /** the name of the large(default: 24x24 px) behance-square image (used for toolbar buttons or icons). */
    public static final String BEHANCE_SQUARE_LARGE = "FontAwesome.behance-square;size=24";

    /** the name of the small(default: 16x16 px) steam image (used for menuitems or buttons). */
    public static final String STEAM_SMALL = "FontAwesome.steam";
    /** the name of the large(default: 24x24 px) steam image (used for toolbar buttons or icons). */
    public static final String STEAM_LARGE = "FontAwesome.steam;size=24";

    /** the name of the small(default: 16x16 px) steam-square image (used for menuitems or buttons). */
    public static final String STEAM_SQUARE_SMALL = "FontAwesome.steam-square";
    /** the name of the large(default: 24x24 px) steam-square image (used for toolbar buttons or icons). */
    public static final String STEAM_SQUARE_LARGE = "FontAwesome.steam-square;size=24";

    /** the name of the small(default: 16x16 px) recycle image (used for menuitems or buttons). */
    public static final String RECYCLE_SMALL = "FontAwesome.recycle";
    /** the name of the large(default: 24x24 px) recycle image (used for toolbar buttons or icons). */
    public static final String RECYCLE_LARGE = "FontAwesome.recycle;size=24";

    /** the name of the small(default: 16x16 px) automobile image (used for menuitems or buttons). */
    public static final String AUTOMOBILE_SMALL = "FontAwesome.automobile";
    /** the name of the large(default: 24x24 px) automobile image (used for toolbar buttons or icons). */
    public static final String AUTOMOBILE_LARGE = "FontAwesome.automobile;size=24";

    /** the name of the small(default: 16x16 px) car image (used for menuitems or buttons). */
    public static final String CAR_SMALL = "FontAwesome.car";
    /** the name of the large(default: 24x24 px) car image (used for toolbar buttons or icons). */
    public static final String CAR_LARGE = "FontAwesome.car;size=24";

    /** the name of the small(default: 16x16 px) cab image (used for menuitems or buttons). */
    public static final String CAB_SMALL = "FontAwesome.cab";
    /** the name of the large(default: 24x24 px) cab image (used for toolbar buttons or icons). */
    public static final String CAB_LARGE = "FontAwesome.cab;size=24";

    /** the name of the small(default: 16x16 px) taxi image (used for menuitems or buttons). */
    public static final String TAXI_SMALL = "FontAwesome.taxi";
    /** the name of the large(default: 24x24 px) taxi image (used for toolbar buttons or icons). */
    public static final String TAXI_LARGE = "FontAwesome.taxi;size=24";

    /** the name of the small(default: 16x16 px) tree image (used for menuitems or buttons). */
    public static final String TREE_SMALL = "FontAwesome.tree";
    /** the name of the large(default: 24x24 px) tree image (used for toolbar buttons or icons). */
    public static final String TREE_LARGE = "FontAwesome.tree;size=24";

    /** the name of the small(default: 16x16 px) spotify image (used for menuitems or buttons). */
    public static final String SPOTIFY_SMALL = "FontAwesome.spotify";
    /** the name of the large(default: 24x24 px) spotify image (used for toolbar buttons or icons). */
    public static final String SPOTIFY_LARGE = "FontAwesome.spotify;size=24";

    /** the name of the small(default: 16x16 px) deviantart image (used for menuitems or buttons). */
    public static final String DEVIANTART_SMALL = "FontAwesome.deviantart";
    /** the name of the large(default: 24x24 px) deviantart image (used for toolbar buttons or icons). */
    public static final String DEVIANTART_LARGE = "FontAwesome.deviantart;size=24";

    /** the name of the small(default: 16x16 px) soundcloud image (used for menuitems or buttons). */
    public static final String SOUNDCLOUD_SMALL = "FontAwesome.soundcloud";
    /** the name of the large(default: 24x24 px) soundcloud image (used for toolbar buttons or icons). */
    public static final String SOUNDCLOUD_LARGE = "FontAwesome.soundcloud;size=24";

    /** the name of the small(default: 16x16 px) database image (used for menuitems or buttons). */
    public static final String DATABASE_SMALL = "FontAwesome.database";
    /** the name of the large(default: 24x24 px) database image (used for toolbar buttons or icons). */
    public static final String DATABASE_LARGE = "FontAwesome.database;size=24";

    /** the name of the small(default: 16x16 px) file-pdf-o image (used for menuitems or buttons). */
    public static final String FILE_PDF_O_SMALL = "FontAwesome.file-pdf-o";
    /** the name of the large(default: 24x24 px) file-pdf-o image (used for toolbar buttons or icons). */
    public static final String FILE_PDF_O_LARGE = "FontAwesome.file-pdf-o;size=24";

    /** the name of the small(default: 16x16 px) file-word-o image (used for menuitems or buttons). */
    public static final String FILE_WORD_O_SMALL = "FontAwesome.file-word-o";
    /** the name of the large(default: 24x24 px) file-word-o image (used for toolbar buttons or icons). */
    public static final String FILE_WORD_O_LARGE = "FontAwesome.file-word-o;size=24";

    /** the name of the small(default: 16x16 px) file-excel-o image (used for menuitems or buttons). */
    public static final String FILE_EXCEL_O_SMALL = "FontAwesome.file-excel-o";
    /** the name of the large(default: 24x24 px) file-excel-o image (used for toolbar buttons or icons). */
    public static final String FILE_EXCEL_O_LARGE = "FontAwesome.file-excel-o;size=24";

    /** the name of the small(default: 16x16 px) file-powerpoint-o image (used for menuitems or buttons). */
    public static final String FILE_POWERPOINT_O_SMALL = "FontAwesome.file-powerpoint-o";
    /** the name of the large(default: 24x24 px) file-powerpoint-o image (used for toolbar buttons or icons). */
    public static final String FILE_POWERPOINT_O_LARGE = "FontAwesome.file-powerpoint-o;size=24";

    /** the name of the small(default: 16x16 px) file-photo-o image (used for menuitems or buttons). */
    public static final String FILE_PHOTO_O_SMALL = "FontAwesome.file-photo-o";
    /** the name of the large(default: 24x24 px) file-photo-o image (used for toolbar buttons or icons). */
    public static final String FILE_PHOTO_O_LARGE = "FontAwesome.file-photo-o;size=24";

    /** the name of the small(default: 16x16 px) file-picture-o image (used for menuitems or buttons). */
    public static final String FILE_PICTURE_O_SMALL = "FontAwesome.file-picture-o";
    /** the name of the large(default: 24x24 px) file-picture-o image (used for toolbar buttons or icons). */
    public static final String FILE_PICTURE_O_LARGE = "FontAwesome.file-picture-o;size=24";

    /** the name of the small(default: 16x16 px) file-image-o image (used for menuitems or buttons). */
    public static final String FILE_IMAGE_O_SMALL = "FontAwesome.file-image-o";
    /** the name of the large(default: 24x24 px) file-image-o image (used for toolbar buttons or icons). */
    public static final String FILE_IMAGE_O_LARGE = "FontAwesome.file-image-o;size=24";

    /** the name of the small(default: 16x16 px) file-zip-o image (used for menuitems or buttons). */
    public static final String FILE_ZIP_O_SMALL = "FontAwesome.file-zip-o";
    /** the name of the large(default: 24x24 px) file-zip-o image (used for toolbar buttons or icons). */
    public static final String FILE_ZIP_O_LARGE = "FontAwesome.file-zip-o;size=24";

    /** the name of the small(default: 16x16 px) file-archive-o image (used for menuitems or buttons). */
    public static final String FILE_ARCHIVE_O_SMALL = "FontAwesome.file-archive-o";
    /** the name of the large(default: 24x24 px) file-archive-o image (used for toolbar buttons or icons). */
    public static final String FILE_ARCHIVE_O_LARGE = "FontAwesome.file-archive-o;size=24";

    /** the name of the small(default: 16x16 px) file-sound-o image (used for menuitems or buttons). */
    public static final String FILE_SOUND_O_SMALL = "FontAwesome.file-sound-o";
    /** the name of the large(default: 24x24 px) file-sound-o image (used for toolbar buttons or icons). */
    public static final String FILE_SOUND_O_LARGE = "FontAwesome.file-sound-o;size=24";

    /** the name of the small(default: 16x16 px) file-audio-o image (used for menuitems or buttons). */
    public static final String FILE_AUDIO_O_SMALL = "FontAwesome.file-audio-o";
    /** the name of the large(default: 24x24 px) file-audio-o image (used for toolbar buttons or icons). */
    public static final String FILE_AUDIO_O_LARGE = "FontAwesome.file-audio-o;size=24";

    /** the name of the small(default: 16x16 px) file-movie-o image (used for menuitems or buttons). */
    public static final String FILE_MOVIE_O_SMALL = "FontAwesome.file-movie-o";
    /** the name of the large(default: 24x24 px) file-movie-o image (used for toolbar buttons or icons). */
    public static final String FILE_MOVIE_O_LARGE = "FontAwesome.file-movie-o;size=24";

    /** the name of the small(default: 16x16 px) file-video-o image (used for menuitems or buttons). */
    public static final String FILE_VIDEO_O_SMALL = "FontAwesome.file-video-o";
    /** the name of the large(default: 24x24 px) file-video-o image (used for toolbar buttons or icons). */
    public static final String FILE_VIDEO_O_LARGE = "FontAwesome.file-video-o;size=24";

    /** the name of the small(default: 16x16 px) file-code-o image (used for menuitems or buttons). */
    public static final String FILE_CODE_O_SMALL = "FontAwesome.file-code-o";
    /** the name of the large(default: 24x24 px) file-code-o image (used for toolbar buttons or icons). */
    public static final String FILE_CODE_O_LARGE = "FontAwesome.file-code-o;size=24";

    /** the name of the small(default: 16x16 px) vine image (used for menuitems or buttons). */
    public static final String VINE_SMALL = "FontAwesome.vine";
    /** the name of the large(default: 24x24 px) vine image (used for toolbar buttons or icons). */
    public static final String VINE_LARGE = "FontAwesome.vine;size=24";

    /** the name of the small(default: 16x16 px) codepen image (used for menuitems or buttons). */
    public static final String CODEPEN_SMALL = "FontAwesome.codepen";
    /** the name of the large(default: 24x24 px) codepen image (used for toolbar buttons or icons). */
    public static final String CODEPEN_LARGE = "FontAwesome.codepen;size=24";

    /** the name of the small(default: 16x16 px) jsfiddle image (used for menuitems or buttons). */
    public static final String JSFIDDLE_SMALL = "FontAwesome.jsfiddle";
    /** the name of the large(default: 24x24 px) jsfiddle image (used for toolbar buttons or icons). */
    public static final String JSFIDDLE_LARGE = "FontAwesome.jsfiddle;size=24";

    /** the name of the small(default: 16x16 px) life-bouy image (used for menuitems or buttons). */
    public static final String LIFE_BOUY_SMALL = "FontAwesome.life-bouy";
    /** the name of the large(default: 24x24 px) life-bouy image (used for toolbar buttons or icons). */
    public static final String LIFE_BOUY_LARGE = "FontAwesome.life-bouy;size=24";

    /** the name of the small(default: 16x16 px) life-buoy image (used for menuitems or buttons). */
    public static final String LIFE_BUOY_SMALL = "FontAwesome.life-buoy";
    /** the name of the large(default: 24x24 px) life-buoy image (used for toolbar buttons or icons). */
    public static final String LIFE_BUOY_LARGE = "FontAwesome.life-buoy;size=24";

    /** the name of the small(default: 16x16 px) life-saver image (used for menuitems or buttons). */
    public static final String LIFE_SAVER_SMALL = "FontAwesome.life-saver";
    /** the name of the large(default: 24x24 px) life-saver image (used for toolbar buttons or icons). */
    public static final String LIFE_SAVER_LARGE = "FontAwesome.life-saver;size=24";

    /** the name of the small(default: 16x16 px) support image (used for menuitems or buttons). */
    public static final String SUPPORT_SMALL = "FontAwesome.support";
    /** the name of the large(default: 24x24 px) support image (used for toolbar buttons or icons). */
    public static final String SUPPORT_LARGE = "FontAwesome.support;size=24";

    /** the name of the small(default: 16x16 px) life-ring image (used for menuitems or buttons). */
    public static final String LIFE_RING_SMALL = "FontAwesome.life-ring";
    /** the name of the large(default: 24x24 px) life-ring image (used for toolbar buttons or icons). */
    public static final String LIFE_RING_LARGE = "FontAwesome.life-ring;size=24";

    /** the name of the small(default: 16x16 px) circle-o-notch image (used for menuitems or buttons). */
    public static final String CIRCLE_O_NOTCH_SMALL = "FontAwesome.circle-o-notch";
    /** the name of the large(default: 24x24 px) circle-o-notch image (used for toolbar buttons or icons). */
    public static final String CIRCLE_O_NOTCH_LARGE = "FontAwesome.circle-o-notch;size=24";

    /** the name of the small(default: 16x16 px) ra image (used for menuitems or buttons). */
    public static final String RA_SMALL = "FontAwesome.ra";
    /** the name of the large(default: 24x24 px) ra image (used for toolbar buttons or icons). */
    public static final String RA_LARGE = "FontAwesome.ra;size=24";

    /** the name of the small(default: 16x16 px) rebel image (used for menuitems or buttons). */
    public static final String REBEL_SMALL = "FontAwesome.rebel";
    /** the name of the large(default: 24x24 px) rebel image (used for toolbar buttons or icons). */
    public static final String REBEL_LARGE = "FontAwesome.rebel;size=24";

    /** the name of the small(default: 16x16 px) ge image (used for menuitems or buttons). */
    public static final String GE_SMALL = "FontAwesome.ge";
    /** the name of the large(default: 24x24 px) ge image (used for toolbar buttons or icons). */
    public static final String GE_LARGE = "FontAwesome.ge;size=24";

    /** the name of the small(default: 16x16 px) empire image (used for menuitems or buttons). */
    public static final String EMPIRE_SMALL = "FontAwesome.empire";
    /** the name of the large(default: 24x24 px) empire image (used for toolbar buttons or icons). */
    public static final String EMPIRE_LARGE = "FontAwesome.empire;size=24";

    /** the name of the small(default: 16x16 px) git-square image (used for menuitems or buttons). */
    public static final String GIT_SQUARE_SMALL = "FontAwesome.git-square";
    /** the name of the large(default: 24x24 px) git-square image (used for toolbar buttons or icons). */
    public static final String GIT_SQUARE_LARGE = "FontAwesome.git-square;size=24";

    /** the name of the small(default: 16x16 px) git image (used for menuitems or buttons). */
    public static final String GIT_SMALL = "FontAwesome.git";
    /** the name of the large(default: 24x24 px) git image (used for toolbar buttons or icons). */
    public static final String GIT_LARGE = "FontAwesome.git;size=24";

    /** the name of the small(default: 16x16 px) y-combinator-square image (used for menuitems or buttons). */
    public static final String Y_COMBINATOR_SQUARE_SMALL = "FontAwesome.y-combinator-square";
    /** the name of the large(default: 24x24 px) y-combinator-square image (used for toolbar buttons or icons). */
    public static final String Y_COMBINATOR_SQUARE_LARGE = "FontAwesome.y-combinator-square;size=24";

    /** the name of the small(default: 16x16 px) yc-square image (used for menuitems or buttons). */
    public static final String YC_SQUARE_SMALL = "FontAwesome.yc-square";
    /** the name of the large(default: 24x24 px) yc-square image (used for toolbar buttons or icons). */
    public static final String YC_SQUARE_LARGE = "FontAwesome.yc-square;size=24";

    /** the name of the small(default: 16x16 px) hacker-news image (used for menuitems or buttons). */
    public static final String HACKER_NEWS_SMALL = "FontAwesome.hacker-news";
    /** the name of the large(default: 24x24 px) hacker-news image (used for toolbar buttons or icons). */
    public static final String HACKER_NEWS_LARGE = "FontAwesome.hacker-news;size=24";

    /** the name of the small(default: 16x16 px) tencent-weibo image (used for menuitems or buttons). */
    public static final String TENCENT_WEIBO_SMALL = "FontAwesome.tencent-weibo";
    /** the name of the large(default: 24x24 px) tencent-weibo image (used for toolbar buttons or icons). */
    public static final String TENCENT_WEIBO_LARGE = "FontAwesome.tencent-weibo;size=24";

    /** the name of the small(default: 16x16 px) qq image (used for menuitems or buttons). */
    public static final String QQ_SMALL = "FontAwesome.qq";
    /** the name of the large(default: 24x24 px) qq image (used for toolbar buttons or icons). */
    public static final String QQ_LARGE = "FontAwesome.qq;size=24";

    /** the name of the small(default: 16x16 px) wechat image (used for menuitems or buttons). */
    public static final String WECHAT_SMALL = "FontAwesome.wechat";
    /** the name of the large(default: 24x24 px) wechat image (used for toolbar buttons or icons). */
    public static final String WECHAT_LARGE = "FontAwesome.wechat;size=24";

    /** the name of the small(default: 16x16 px) weixin image (used for menuitems or buttons). */
    public static final String WEIXIN_SMALL = "FontAwesome.weixin";
    /** the name of the large(default: 24x24 px) weixin image (used for toolbar buttons or icons). */
    public static final String WEIXIN_LARGE = "FontAwesome.weixin;size=24";

    /** the name of the small(default: 16x16 px) send image (used for menuitems or buttons). */
    public static final String SEND_SMALL = "FontAwesome.send";
    /** the name of the large(default: 24x24 px) send image (used for toolbar buttons or icons). */
    public static final String SEND_LARGE = "FontAwesome.send;size=24";

    /** the name of the small(default: 16x16 px) paper-plane image (used for menuitems or buttons). */
    public static final String PAPER_PLANE_SMALL = "FontAwesome.paper-plane";
    /** the name of the large(default: 24x24 px) paper-plane image (used for toolbar buttons or icons). */
    public static final String PAPER_PLANE_LARGE = "FontAwesome.paper-plane;size=24";

    /** the name of the small(default: 16x16 px) send-o image (used for menuitems or buttons). */
    public static final String SEND_O_SMALL = "FontAwesome.send-o";
    /** the name of the large(default: 24x24 px) send-o image (used for toolbar buttons or icons). */
    public static final String SEND_O_LARGE = "FontAwesome.send-o;size=24";

    /** the name of the small(default: 16x16 px) paper-plane-o image (used for menuitems or buttons). */
    public static final String PAPER_PLANE_O_SMALL = "FontAwesome.paper-plane-o";
    /** the name of the large(default: 24x24 px) paper-plane-o image (used for toolbar buttons or icons). */
    public static final String PAPER_PLANE_O_LARGE = "FontAwesome.paper-plane-o;size=24";

    /** the name of the small(default: 16x16 px) history image (used for menuitems or buttons). */
    public static final String HISTORY_SMALL = "FontAwesome.history";
    /** the name of the large(default: 24x24 px) history image (used for toolbar buttons or icons). */
    public static final String HISTORY_LARGE = "FontAwesome.history;size=24";

    /** the name of the small(default: 16x16 px) circle-thin image (used for menuitems or buttons). */
    public static final String CIRCLE_THIN_SMALL = "FontAwesome.circle-thin";
    /** the name of the large(default: 24x24 px) circle-thin image (used for toolbar buttons or icons). */
    public static final String CIRCLE_THIN_LARGE = "FontAwesome.circle-thin;size=24";

    /** the name of the small(default: 16x16 px) header image (used for menuitems or buttons). */
    public static final String HEADER_SMALL = "FontAwesome.header";
    /** the name of the large(default: 24x24 px) header image (used for toolbar buttons or icons). */
    public static final String HEADER_LARGE = "FontAwesome.header;size=24";

    /** the name of the small(default: 16x16 px) paragraph image (used for menuitems or buttons). */
    public static final String PARAGRAPH_SMALL = "FontAwesome.paragraph";
    /** the name of the large(default: 24x24 px) paragraph image (used for toolbar buttons or icons). */
    public static final String PARAGRAPH_LARGE = "FontAwesome.paragraph;size=24";

    /** the name of the small(default: 16x16 px) sliders image (used for menuitems or buttons). */
    public static final String SLIDERS_SMALL = "FontAwesome.sliders";
    /** the name of the large(default: 24x24 px) sliders image (used for toolbar buttons or icons). */
    public static final String SLIDERS_LARGE = "FontAwesome.sliders;size=24";

    /** the name of the small(default: 16x16 px) share-alt image (used for menuitems or buttons). */
    public static final String SHARE_ALT_SMALL = "FontAwesome.share-alt";
    /** the name of the large(default: 24x24 px) share-alt image (used for toolbar buttons or icons). */
    public static final String SHARE_ALT_LARGE = "FontAwesome.share-alt;size=24";

    /** the name of the small(default: 16x16 px) share-alt-square image (used for menuitems or buttons). */
    public static final String SHARE_ALT_SQUARE_SMALL = "FontAwesome.share-alt-square";
    /** the name of the large(default: 24x24 px) share-alt-square image (used for toolbar buttons or icons). */
    public static final String SHARE_ALT_SQUARE_LARGE = "FontAwesome.share-alt-square;size=24";

    /** the name of the small(default: 16x16 px) bomb image (used for menuitems or buttons). */
    public static final String BOMB_SMALL = "FontAwesome.bomb";
    /** the name of the large(default: 24x24 px) bomb image (used for toolbar buttons or icons). */
    public static final String BOMB_LARGE = "FontAwesome.bomb;size=24";

    /** the name of the small(default: 16x16 px) soccer-ball-o image (used for menuitems or buttons). */
    public static final String SOCCER_BALL_O_SMALL = "FontAwesome.soccer-ball-o";
    /** the name of the large(default: 24x24 px) soccer-ball-o image (used for toolbar buttons or icons). */
    public static final String SOCCER_BALL_O_LARGE = "FontAwesome.soccer-ball-o;size=24";

    /** the name of the small(default: 16x16 px) futbol-o image (used for menuitems or buttons). */
    public static final String FUTBOL_O_SMALL = "FontAwesome.futbol-o";
    /** the name of the large(default: 24x24 px) futbol-o image (used for toolbar buttons or icons). */
    public static final String FUTBOL_O_LARGE = "FontAwesome.futbol-o;size=24";

    /** the name of the small(default: 16x16 px) tty image (used for menuitems or buttons). */
    public static final String TTY_SMALL = "FontAwesome.tty";
    /** the name of the large(default: 24x24 px) tty image (used for toolbar buttons or icons). */
    public static final String TTY_LARGE = "FontAwesome.tty;size=24";

    /** the name of the small(default: 16x16 px) binoculars image (used for menuitems or buttons). */
    public static final String BINOCULARS_SMALL = "FontAwesome.binoculars";
    /** the name of the large(default: 24x24 px) binoculars image (used for toolbar buttons or icons). */
    public static final String BINOCULARS_LARGE = "FontAwesome.binoculars;size=24";

    /** the name of the small(default: 16x16 px) plug image (used for menuitems or buttons). */
    public static final String PLUG_SMALL = "FontAwesome.plug";
    /** the name of the large(default: 24x24 px) plug image (used for toolbar buttons or icons). */
    public static final String PLUG_LARGE = "FontAwesome.plug;size=24";

    /** the name of the small(default: 16x16 px) slideshare image (used for menuitems or buttons). */
    public static final String SLIDESHARE_SMALL = "FontAwesome.slideshare";
    /** the name of the large(default: 24x24 px) slideshare image (used for toolbar buttons or icons). */
    public static final String SLIDESHARE_LARGE = "FontAwesome.slideshare;size=24";

    /** the name of the small(default: 16x16 px) twitch image (used for menuitems or buttons). */
    public static final String TWITCH_SMALL = "FontAwesome.twitch";
    /** the name of the large(default: 24x24 px) twitch image (used for toolbar buttons or icons). */
    public static final String TWITCH_LARGE = "FontAwesome.twitch;size=24";

    /** the name of the small(default: 16x16 px) yelp image (used for menuitems or buttons). */
    public static final String YELP_SMALL = "FontAwesome.yelp";
    /** the name of the large(default: 24x24 px) yelp image (used for toolbar buttons or icons). */
    public static final String YELP_LARGE = "FontAwesome.yelp;size=24";

    /** the name of the small(default: 16x16 px) newspaper-o image (used for menuitems or buttons). */
    public static final String NEWSPAPER_O_SMALL = "FontAwesome.newspaper-o";
    /** the name of the large(default: 24x24 px) newspaper-o image (used for toolbar buttons or icons). */
    public static final String NEWSPAPER_O_LARGE = "FontAwesome.newspaper-o;size=24";

    /** the name of the small(default: 16x16 px) wifi image (used for menuitems or buttons). */
    public static final String WIFI_SMALL = "FontAwesome.wifi";
    /** the name of the large(default: 24x24 px) wifi image (used for toolbar buttons or icons). */
    public static final String WIFI_LARGE = "FontAwesome.wifi;size=24";

    /** the name of the small(default: 16x16 px) calculator image (used for menuitems or buttons). */
    public static final String CALCULATOR_SMALL = "FontAwesome.calculator";
    /** the name of the large(default: 24x24 px) calculator image (used for toolbar buttons or icons). */
    public static final String CALCULATOR_LARGE = "FontAwesome.calculator;size=24";

    /** the name of the small(default: 16x16 px) paypal image (used for menuitems or buttons). */
    public static final String PAYPAL_SMALL = "FontAwesome.paypal";
    /** the name of the large(default: 24x24 px) paypal image (used for toolbar buttons or icons). */
    public static final String PAYPAL_LARGE = "FontAwesome.paypal;size=24";

    /** the name of the small(default: 16x16 px) google-wallet image (used for menuitems or buttons). */
    public static final String GOOGLE_WALLET_SMALL = "FontAwesome.google-wallet";
    /** the name of the large(default: 24x24 px) google-wallet image (used for toolbar buttons or icons). */
    public static final String GOOGLE_WALLET_LARGE = "FontAwesome.google-wallet;size=24";

    /** the name of the small(default: 16x16 px) cc-visa image (used for menuitems or buttons). */
    public static final String CC_VISA_SMALL = "FontAwesome.cc-visa";
    /** the name of the large(default: 24x24 px) cc-visa image (used for toolbar buttons or icons). */
    public static final String CC_VISA_LARGE = "FontAwesome.cc-visa;size=24";

    /** the name of the small(default: 16x16 px) cc-mastercard image (used for menuitems or buttons). */
    public static final String CC_MASTERCARD_SMALL = "FontAwesome.cc-mastercard";
    /** the name of the large(default: 24x24 px) cc-mastercard image (used for toolbar buttons or icons). */
    public static final String CC_MASTERCARD_LARGE = "FontAwesome.cc-mastercard;size=24";

    /** the name of the small(default: 16x16 px) cc-discover image (used for menuitems or buttons). */
    public static final String CC_DISCOVER_SMALL = "FontAwesome.cc-discover";
    /** the name of the large(default: 24x24 px) cc-discover image (used for toolbar buttons or icons). */
    public static final String CC_DISCOVER_LARGE = "FontAwesome.cc-discover;size=24";

    /** the name of the small(default: 16x16 px) cc-amex image (used for menuitems or buttons). */
    public static final String CC_AMEX_SMALL = "FontAwesome.cc-amex";
    /** the name of the large(default: 24x24 px) cc-amex image (used for toolbar buttons or icons). */
    public static final String CC_AMEX_LARGE = "FontAwesome.cc-amex;size=24";

    /** the name of the small(default: 16x16 px) cc-paypal image (used for menuitems or buttons). */
    public static final String CC_PAYPAL_SMALL = "FontAwesome.cc-paypal";
    /** the name of the large(default: 24x24 px) cc-paypal image (used for toolbar buttons or icons). */
    public static final String CC_PAYPAL_LARGE = "FontAwesome.cc-paypal;size=24";

    /** the name of the small(default: 16x16 px) cc-stripe image (used for menuitems or buttons). */
    public static final String CC_STRIPE_SMALL = "FontAwesome.cc-stripe";
    /** the name of the large(default: 24x24 px) cc-stripe image (used for toolbar buttons or icons). */
    public static final String CC_STRIPE_LARGE = "FontAwesome.cc-stripe;size=24";

    /** the name of the small(default: 16x16 px) bell-slash image (used for menuitems or buttons). */
    public static final String BELL_SLASH_SMALL = "FontAwesome.bell-slash";
    /** the name of the large(default: 24x24 px) bell-slash image (used for toolbar buttons or icons). */
    public static final String BELL_SLASH_LARGE = "FontAwesome.bell-slash;size=24";

    /** the name of the small(default: 16x16 px) bell-slash-o image (used for menuitems or buttons). */
    public static final String BELL_SLASH_O_SMALL = "FontAwesome.bell-slash-o";
    /** the name of the large(default: 24x24 px) bell-slash-o image (used for toolbar buttons or icons). */
    public static final String BELL_SLASH_O_LARGE = "FontAwesome.bell-slash-o;size=24";

    /** the name of the small(default: 16x16 px) trash image (used for menuitems or buttons). */
    public static final String TRASH_SMALL = "FontAwesome.trash";
    /** the name of the large(default: 24x24 px) trash image (used for toolbar buttons or icons). */
    public static final String TRASH_LARGE = "FontAwesome.trash;size=24";

    /** the name of the small(default: 16x16 px) copyright image (used for menuitems or buttons). */
    public static final String COPYRIGHT_SMALL = "FontAwesome.copyright";
    /** the name of the large(default: 24x24 px) copyright image (used for toolbar buttons or icons). */
    public static final String COPYRIGHT_LARGE = "FontAwesome.copyright;size=24";

    /** the name of the small(default: 16x16 px) at image (used for menuitems or buttons). */
    public static final String AT_SMALL = "FontAwesome.at";
    /** the name of the large(default: 24x24 px) at image (used for toolbar buttons or icons). */
    public static final String AT_LARGE = "FontAwesome.at;size=24";

    /** the name of the small(default: 16x16 px) eyedropper image (used for menuitems or buttons). */
    public static final String EYEDROPPER_SMALL = "FontAwesome.eyedropper";
    /** the name of the large(default: 24x24 px) eyedropper image (used for toolbar buttons or icons). */
    public static final String EYEDROPPER_LARGE = "FontAwesome.eyedropper;size=24";

    /** the name of the small(default: 16x16 px) paint-brush image (used for menuitems or buttons). */
    public static final String PAINT_BRUSH_SMALL = "FontAwesome.paint-brush";
    /** the name of the large(default: 24x24 px) paint-brush image (used for toolbar buttons or icons). */
    public static final String PAINT_BRUSH_LARGE = "FontAwesome.paint-brush;size=24";

    /** the name of the small(default: 16x16 px) birthday-cake image (used for menuitems or buttons). */
    public static final String BIRTHDAY_CAKE_SMALL = "FontAwesome.birthday-cake";
    /** the name of the large(default: 24x24 px) birthday-cake image (used for toolbar buttons or icons). */
    public static final String BIRTHDAY_CAKE_LARGE = "FontAwesome.birthday-cake;size=24";

    /** the name of the small(default: 16x16 px) area-chart image (used for menuitems or buttons). */
    public static final String AREA_CHART_SMALL = "FontAwesome.area-chart";
    /** the name of the large(default: 24x24 px) area-chart image (used for toolbar buttons or icons). */
    public static final String AREA_CHART_LARGE = "FontAwesome.area-chart;size=24";

    /** the name of the small(default: 16x16 px) pie-chart image (used for menuitems or buttons). */
    public static final String PIE_CHART_SMALL = "FontAwesome.pie-chart";
    /** the name of the large(default: 24x24 px) pie-chart image (used for toolbar buttons or icons). */
    public static final String PIE_CHART_LARGE = "FontAwesome.pie-chart;size=24";

    /** the name of the small(default: 16x16 px) line-chart image (used for menuitems or buttons). */
    public static final String LINE_CHART_SMALL = "FontAwesome.line-chart";
    /** the name of the large(default: 24x24 px) line-chart image (used for toolbar buttons or icons). */
    public static final String LINE_CHART_LARGE = "FontAwesome.line-chart;size=24";

    /** the name of the small(default: 16x16 px) lastfm image (used for menuitems or buttons). */
    public static final String LASTFM_SMALL = "FontAwesome.lastfm";
    /** the name of the large(default: 24x24 px) lastfm image (used for toolbar buttons or icons). */
    public static final String LASTFM_LARGE = "FontAwesome.lastfm;size=24";

    /** the name of the small(default: 16x16 px) lastfm-square image (used for menuitems or buttons). */
    public static final String LASTFM_SQUARE_SMALL = "FontAwesome.lastfm-square";
    /** the name of the large(default: 24x24 px) lastfm-square image (used for toolbar buttons or icons). */
    public static final String LASTFM_SQUARE_LARGE = "FontAwesome.lastfm-square;size=24";

    /** the name of the small(default: 16x16 px) toggle-off image (used for menuitems or buttons). */
    public static final String TOGGLE_OFF_SMALL = "FontAwesome.toggle-off";
    /** the name of the large(default: 24x24 px) toggle-off image (used for toolbar buttons or icons). */
    public static final String TOGGLE_OFF_LARGE = "FontAwesome.toggle-off;size=24";

    /** the name of the small(default: 16x16 px) toggle-on image (used for menuitems or buttons). */
    public static final String TOGGLE_ON_SMALL = "FontAwesome.toggle-on";
    /** the name of the large(default: 24x24 px) toggle-on image (used for toolbar buttons or icons). */
    public static final String TOGGLE_ON_LARGE = "FontAwesome.toggle-on;size=24";

    /** the name of the small(default: 16x16 px) bicycle image (used for menuitems or buttons). */
    public static final String BICYCLE_SMALL = "FontAwesome.bicycle";
    /** the name of the large(default: 24x24 px) bicycle image (used for toolbar buttons or icons). */
    public static final String BICYCLE_LARGE = "FontAwesome.bicycle;size=24";

    /** the name of the small(default: 16x16 px) bus image (used for menuitems or buttons). */
    public static final String BUS_SMALL = "FontAwesome.bus";
    /** the name of the large(default: 24x24 px) bus image (used for toolbar buttons or icons). */
    public static final String BUS_LARGE = "FontAwesome.bus;size=24";

    /** the name of the small(default: 16x16 px) ioxhost image (used for menuitems or buttons). */
    public static final String IOXHOST_SMALL = "FontAwesome.ioxhost";
    /** the name of the large(default: 24x24 px) ioxhost image (used for toolbar buttons or icons). */
    public static final String IOXHOST_LARGE = "FontAwesome.ioxhost;size=24";

    /** the name of the small(default: 16x16 px) angellist image (used for menuitems or buttons). */
    public static final String ANGELLIST_SMALL = "FontAwesome.angellist";
    /** the name of the large(default: 24x24 px) angellist image (used for toolbar buttons or icons). */
    public static final String ANGELLIST_LARGE = "FontAwesome.angellist;size=24";

    /** the name of the small(default: 16x16 px) cc image (used for menuitems or buttons). */
    public static final String CC_SMALL = "FontAwesome.cc";
    /** the name of the large(default: 24x24 px) cc image (used for toolbar buttons or icons). */
    public static final String CC_LARGE = "FontAwesome.cc;size=24";

    /** the name of the small(default: 16x16 px) shekel image (used for menuitems or buttons). */
    public static final String SHEKEL_SMALL = "FontAwesome.shekel";
    /** the name of the large(default: 24x24 px) shekel image (used for toolbar buttons or icons). */
    public static final String SHEKEL_LARGE = "FontAwesome.shekel;size=24";

    /** the name of the small(default: 16x16 px) sheqel image (used for menuitems or buttons). */
    public static final String SHEQEL_SMALL = "FontAwesome.sheqel";
    /** the name of the large(default: 24x24 px) sheqel image (used for toolbar buttons or icons). */
    public static final String SHEQEL_LARGE = "FontAwesome.sheqel;size=24";

    /** the name of the small(default: 16x16 px) ils image (used for menuitems or buttons). */
    public static final String ILS_SMALL = "FontAwesome.ils";
    /** the name of the large(default: 24x24 px) ils image (used for toolbar buttons or icons). */
    public static final String ILS_LARGE = "FontAwesome.ils;size=24";

    /** the name of the small(default: 16x16 px) meanpath image (used for menuitems or buttons). */
    public static final String MEANPATH_SMALL = "FontAwesome.meanpath";
    /** the name of the large(default: 24x24 px) meanpath image (used for toolbar buttons or icons). */
    public static final String MEANPATH_LARGE = "FontAwesome.meanpath;size=24";

    /** the name of the small(default: 16x16 px) buysellads image (used for menuitems or buttons). */
    public static final String BUYSELLADS_SMALL = "FontAwesome.buysellads";
    /** the name of the large(default: 24x24 px) buysellads image (used for toolbar buttons or icons). */
    public static final String BUYSELLADS_LARGE = "FontAwesome.buysellads;size=24";

    /** the name of the small(default: 16x16 px) connectdevelop image (used for menuitems or buttons). */
    public static final String CONNECTDEVELOP_SMALL = "FontAwesome.connectdevelop";
    /** the name of the large(default: 24x24 px) connectdevelop image (used for toolbar buttons or icons). */
    public static final String CONNECTDEVELOP_LARGE = "FontAwesome.connectdevelop;size=24";

    /** the name of the small(default: 16x16 px) dashcube image (used for menuitems or buttons). */
    public static final String DASHCUBE_SMALL = "FontAwesome.dashcube";
    /** the name of the large(default: 24x24 px) dashcube image (used for toolbar buttons or icons). */
    public static final String DASHCUBE_LARGE = "FontAwesome.dashcube;size=24";

    /** the name of the small(default: 16x16 px) forumbee image (used for menuitems or buttons). */
    public static final String FORUMBEE_SMALL = "FontAwesome.forumbee";
    /** the name of the large(default: 24x24 px) forumbee image (used for toolbar buttons or icons). */
    public static final String FORUMBEE_LARGE = "FontAwesome.forumbee;size=24";

    /** the name of the small(default: 16x16 px) leanpub image (used for menuitems or buttons). */
    public static final String LEANPUB_SMALL = "FontAwesome.leanpub";
    /** the name of the large(default: 24x24 px) leanpub image (used for toolbar buttons or icons). */
    public static final String LEANPUB_LARGE = "FontAwesome.leanpub;size=24";

    /** the name of the small(default: 16x16 px) sellsy image (used for menuitems or buttons). */
    public static final String SELLSY_SMALL = "FontAwesome.sellsy";
    /** the name of the large(default: 24x24 px) sellsy image (used for toolbar buttons or icons). */
    public static final String SELLSY_LARGE = "FontAwesome.sellsy;size=24";

    /** the name of the small(default: 16x16 px) shirtsinbulk image (used for menuitems or buttons). */
    public static final String SHIRTSINBULK_SMALL = "FontAwesome.shirtsinbulk";
    /** the name of the large(default: 24x24 px) shirtsinbulk image (used for toolbar buttons or icons). */
    public static final String SHIRTSINBULK_LARGE = "FontAwesome.shirtsinbulk;size=24";

    /** the name of the small(default: 16x16 px) simplybuilt image (used for menuitems or buttons). */
    public static final String SIMPLYBUILT_SMALL = "FontAwesome.simplybuilt";
    /** the name of the large(default: 24x24 px) simplybuilt image (used for toolbar buttons or icons). */
    public static final String SIMPLYBUILT_LARGE = "FontAwesome.simplybuilt;size=24";

    /** the name of the small(default: 16x16 px) skyatlas image (used for menuitems or buttons). */
    public static final String SKYATLAS_SMALL = "FontAwesome.skyatlas";
    /** the name of the large(default: 24x24 px) skyatlas image (used for toolbar buttons or icons). */
    public static final String SKYATLAS_LARGE = "FontAwesome.skyatlas;size=24";

    /** the name of the small(default: 16x16 px) cart-plus image (used for menuitems or buttons). */
    public static final String CART_PLUS_SMALL = "FontAwesome.cart-plus";
    /** the name of the large(default: 24x24 px) cart-plus image (used for toolbar buttons or icons). */
    public static final String CART_PLUS_LARGE = "FontAwesome.cart-plus;size=24";

    /** the name of the small(default: 16x16 px) cart-arrow-down image (used for menuitems or buttons). */
    public static final String CART_ARROW_DOWN_SMALL = "FontAwesome.cart-arrow-down";
    /** the name of the large(default: 24x24 px) cart-arrow-down image (used for toolbar buttons or icons). */
    public static final String CART_ARROW_DOWN_LARGE = "FontAwesome.cart-arrow-down;size=24";

    /** the name of the small(default: 16x16 px) diamond image (used for menuitems or buttons). */
    public static final String DIAMOND_SMALL = "FontAwesome.diamond";
    /** the name of the large(default: 24x24 px) diamond image (used for toolbar buttons or icons). */
    public static final String DIAMOND_LARGE = "FontAwesome.diamond;size=24";

    /** the name of the small(default: 16x16 px) ship image (used for menuitems or buttons). */
    public static final String SHIP_SMALL = "FontAwesome.ship";
    /** the name of the large(default: 24x24 px) ship image (used for toolbar buttons or icons). */
    public static final String SHIP_LARGE = "FontAwesome.ship;size=24";

    /** the name of the small(default: 16x16 px) user-secret image (used for menuitems or buttons). */
    public static final String USER_SECRET_SMALL = "FontAwesome.user-secret";
    /** the name of the large(default: 24x24 px) user-secret image (used for toolbar buttons or icons). */
    public static final String USER_SECRET_LARGE = "FontAwesome.user-secret;size=24";

    /** the name of the small(default: 16x16 px) motorcycle image (used for menuitems or buttons). */
    public static final String MOTORCYCLE_SMALL = "FontAwesome.motorcycle";
    /** the name of the large(default: 24x24 px) motorcycle image (used for toolbar buttons or icons). */
    public static final String MOTORCYCLE_LARGE = "FontAwesome.motorcycle;size=24";

    /** the name of the small(default: 16x16 px) street-view image (used for menuitems or buttons). */
    public static final String STREET_VIEW_SMALL = "FontAwesome.street-view";
    /** the name of the large(default: 24x24 px) street-view image (used for toolbar buttons or icons). */
    public static final String STREET_VIEW_LARGE = "FontAwesome.street-view;size=24";

    /** the name of the small(default: 16x16 px) heartbeat image (used for menuitems or buttons). */
    public static final String HEARTBEAT_SMALL = "FontAwesome.heartbeat";
    /** the name of the large(default: 24x24 px) heartbeat image (used for toolbar buttons or icons). */
    public static final String HEARTBEAT_LARGE = "FontAwesome.heartbeat;size=24";

    /** the name of the small(default: 16x16 px) venus image (used for menuitems or buttons). */
    public static final String VENUS_SMALL = "FontAwesome.venus";
    /** the name of the large(default: 24x24 px) venus image (used for toolbar buttons or icons). */
    public static final String VENUS_LARGE = "FontAwesome.venus;size=24";

    /** the name of the small(default: 16x16 px) mars image (used for menuitems or buttons). */
    public static final String MARS_SMALL = "FontAwesome.mars";
    /** the name of the large(default: 24x24 px) mars image (used for toolbar buttons or icons). */
    public static final String MARS_LARGE = "FontAwesome.mars;size=24";

    /** the name of the small(default: 16x16 px) mercury image (used for menuitems or buttons). */
    public static final String MERCURY_SMALL = "FontAwesome.mercury";
    /** the name of the large(default: 24x24 px) mercury image (used for toolbar buttons or icons). */
    public static final String MERCURY_LARGE = "FontAwesome.mercury;size=24";

    /** the name of the small(default: 16x16 px) intersex image (used for menuitems or buttons). */
    public static final String INTERSEX_SMALL = "FontAwesome.intersex";
    /** the name of the large(default: 24x24 px) intersex image (used for toolbar buttons or icons). */
    public static final String INTERSEX_LARGE = "FontAwesome.intersex;size=24";

    /** the name of the small(default: 16x16 px) transgender image (used for menuitems or buttons). */
    public static final String TRANSGENDER_SMALL = "FontAwesome.transgender";
    /** the name of the large(default: 24x24 px) transgender image (used for toolbar buttons or icons). */
    public static final String TRANSGENDER_LARGE = "FontAwesome.transgender;size=24";

    /** the name of the small(default: 16x16 px) transgender-alt image (used for menuitems or buttons). */
    public static final String TRANSGENDER_ALT_SMALL = "FontAwesome.transgender-alt";
    /** the name of the large(default: 24x24 px) transgender-alt image (used for toolbar buttons or icons). */
    public static final String TRANSGENDER_ALT_LARGE = "FontAwesome.transgender-alt;size=24";

    /** the name of the small(default: 16x16 px) venus-double image (used for menuitems or buttons). */
    public static final String VENUS_DOUBLE_SMALL = "FontAwesome.venus-double";
    /** the name of the large(default: 24x24 px) venus-double image (used for toolbar buttons or icons). */
    public static final String VENUS_DOUBLE_LARGE = "FontAwesome.venus-double;size=24";

    /** the name of the small(default: 16x16 px) mars-double image (used for menuitems or buttons). */
    public static final String MARS_DOUBLE_SMALL = "FontAwesome.mars-double";
    /** the name of the large(default: 24x24 px) mars-double image (used for toolbar buttons or icons). */
    public static final String MARS_DOUBLE_LARGE = "FontAwesome.mars-double;size=24";

    /** the name of the small(default: 16x16 px) venus-mars image (used for menuitems or buttons). */
    public static final String VENUS_MARS_SMALL = "FontAwesome.venus-mars";
    /** the name of the large(default: 24x24 px) venus-mars image (used for toolbar buttons or icons). */
    public static final String VENUS_MARS_LARGE = "FontAwesome.venus-mars;size=24";

    /** the name of the small(default: 16x16 px) mars-stroke image (used for menuitems or buttons). */
    public static final String MARS_STROKE_SMALL = "FontAwesome.mars-stroke";
    /** the name of the large(default: 24x24 px) mars-stroke image (used for toolbar buttons or icons). */
    public static final String MARS_STROKE_LARGE = "FontAwesome.mars-stroke;size=24";

    /** the name of the small(default: 16x16 px) mars-stroke-v image (used for menuitems or buttons). */
    public static final String MARS_STROKE_V_SMALL = "FontAwesome.mars-stroke-v";
    /** the name of the large(default: 24x24 px) mars-stroke-v image (used for toolbar buttons or icons). */
    public static final String MARS_STROKE_V_LARGE = "FontAwesome.mars-stroke-v;size=24";

    /** the name of the small(default: 16x16 px) mars-stroke-h image (used for menuitems or buttons). */
    public static final String MARS_STROKE_H_SMALL = "FontAwesome.mars-stroke-h";
    /** the name of the large(default: 24x24 px) mars-stroke-h image (used for toolbar buttons or icons). */
    public static final String MARS_STROKE_H_LARGE = "FontAwesome.mars-stroke-h;size=24";

    /** the name of the small(default: 16x16 px) neuter image (used for menuitems or buttons). */
    public static final String NEUTER_SMALL = "FontAwesome.neuter";
    /** the name of the large(default: 24x24 px) neuter image (used for toolbar buttons or icons). */
    public static final String NEUTER_LARGE = "FontAwesome.neuter;size=24";

    /** the name of the small(default: 16x16 px) genderless image (used for menuitems or buttons). */
    public static final String GENDERLESS_SMALL = "FontAwesome.genderless";
    /** the name of the large(default: 24x24 px) genderless image (used for toolbar buttons or icons). */
    public static final String GENDERLESS_LARGE = "FontAwesome.genderless;size=24";

    /** the name of the small(default: 16x16 px) facebook-official image (used for menuitems or buttons). */
    public static final String FACEBOOK_OFFICIAL_SMALL = "FontAwesome.facebook-official";
    /** the name of the large(default: 24x24 px) facebook-official image (used for toolbar buttons or icons). */
    public static final String FACEBOOK_OFFICIAL_LARGE = "FontAwesome.facebook-official;size=24";

    /** the name of the small(default: 16x16 px) pinterest-p image (used for menuitems or buttons). */
    public static final String PINTEREST_P_SMALL = "FontAwesome.pinterest-p";
    /** the name of the large(default: 24x24 px) pinterest-p image (used for toolbar buttons or icons). */
    public static final String PINTEREST_P_LARGE = "FontAwesome.pinterest-p;size=24";

    /** the name of the small(default: 16x16 px) whatsapp image (used for menuitems or buttons). */
    public static final String WHATSAPP_SMALL = "FontAwesome.whatsapp";
    /** the name of the large(default: 24x24 px) whatsapp image (used for toolbar buttons or icons). */
    public static final String WHATSAPP_LARGE = "FontAwesome.whatsapp;size=24";

    /** the name of the small(default: 16x16 px) server image (used for menuitems or buttons). */
    public static final String SERVER_SMALL = "FontAwesome.server";
    /** the name of the large(default: 24x24 px) server image (used for toolbar buttons or icons). */
    public static final String SERVER_LARGE = "FontAwesome.server;size=24";

    /** the name of the small(default: 16x16 px) user-plus image (used for menuitems or buttons). */
    public static final String USER_PLUS_SMALL = "FontAwesome.user-plus";
    /** the name of the large(default: 24x24 px) user-plus image (used for toolbar buttons or icons). */
    public static final String USER_PLUS_LARGE = "FontAwesome.user-plus;size=24";

    /** the name of the small(default: 16x16 px) user-times image (used for menuitems or buttons). */
    public static final String USER_TIMES_SMALL = "FontAwesome.user-times";
    /** the name of the large(default: 24x24 px) user-times image (used for toolbar buttons or icons). */
    public static final String USER_TIMES_LARGE = "FontAwesome.user-times;size=24";

    /** the name of the small(default: 16x16 px) hotel image (used for menuitems or buttons). */
    public static final String HOTEL_SMALL = "FontAwesome.hotel";
    /** the name of the large(default: 24x24 px) hotel image (used for toolbar buttons or icons). */
    public static final String HOTEL_LARGE = "FontAwesome.hotel;size=24";

    /** the name of the small(default: 16x16 px) bed image (used for menuitems or buttons). */
    public static final String BED_SMALL = "FontAwesome.bed";
    /** the name of the large(default: 24x24 px) bed image (used for toolbar buttons or icons). */
    public static final String BED_LARGE = "FontAwesome.bed;size=24";

    /** the name of the small(default: 16x16 px) viacoin image (used for menuitems or buttons). */
    public static final String VIACOIN_SMALL = "FontAwesome.viacoin";
    /** the name of the large(default: 24x24 px) viacoin image (used for toolbar buttons or icons). */
    public static final String VIACOIN_LARGE = "FontAwesome.viacoin;size=24";

    /** the name of the small(default: 16x16 px) train image (used for menuitems or buttons). */
    public static final String TRAIN_SMALL = "FontAwesome.train";
    /** the name of the large(default: 24x24 px) train image (used for toolbar buttons or icons). */
    public static final String TRAIN_LARGE = "FontAwesome.train;size=24";

    /** the name of the small(default: 16x16 px) subway image (used for menuitems or buttons). */
    public static final String SUBWAY_SMALL = "FontAwesome.subway";
    /** the name of the large(default: 24x24 px) subway image (used for toolbar buttons or icons). */
    public static final String SUBWAY_LARGE = "FontAwesome.subway;size=24";

    /** the name of the small(default: 16x16 px) medium image (used for menuitems or buttons). */
    public static final String MEDIUM_SMALL = "FontAwesome.medium";
    /** the name of the large(default: 24x24 px) medium image (used for toolbar buttons or icons). */
    public static final String MEDIUM_LARGE = "FontAwesome.medium;size=24";

    /** the name of the small(default: 16x16 px) yc image (used for menuitems or buttons). */
    public static final String YC_SMALL = "FontAwesome.yc";
    /** the name of the large(default: 24x24 px) yc image (used for toolbar buttons or icons). */
    public static final String YC_LARGE = "FontAwesome.yc;size=24";

    /** the name of the small(default: 16x16 px) y-combinator image (used for menuitems or buttons). */
    public static final String Y_COMBINATOR_SMALL = "FontAwesome.y-combinator";
    /** the name of the large(default: 24x24 px) y-combinator image (used for toolbar buttons or icons). */
    public static final String Y_COMBINATOR_LARGE = "FontAwesome.y-combinator;size=24";

    /** the name of the small(default: 16x16 px) optin-monster image (used for menuitems or buttons). */
    public static final String OPTIN_MONSTER_SMALL = "FontAwesome.optin-monster";
    /** the name of the large(default: 24x24 px) optin-monster image (used for toolbar buttons or icons). */
    public static final String OPTIN_MONSTER_LARGE = "FontAwesome.optin-monster;size=24";

    /** the name of the small(default: 16x16 px) opencart image (used for menuitems or buttons). */
    public static final String OPENCART_SMALL = "FontAwesome.opencart";
    /** the name of the large(default: 24x24 px) opencart image (used for toolbar buttons or icons). */
    public static final String OPENCART_LARGE = "FontAwesome.opencart;size=24";

    /** the name of the small(default: 16x16 px) expeditedssl image (used for menuitems or buttons). */
    public static final String EXPEDITEDSSL_SMALL = "FontAwesome.expeditedssl";
    /** the name of the large(default: 24x24 px) expeditedssl image (used for toolbar buttons or icons). */
    public static final String EXPEDITEDSSL_LARGE = "FontAwesome.expeditedssl;size=24";

    /** the name of the small(default: 16x16 px) battery-4 image (used for menuitems or buttons). */
    public static final String BATTERY_4_SMALL = "FontAwesome.battery-4";
    /** the name of the large(default: 24x24 px) battery-4 image (used for toolbar buttons or icons). */
    public static final String BATTERY_4_LARGE = "FontAwesome.battery-4;size=24";

    /** the name of the small(default: 16x16 px) battery-full image (used for menuitems or buttons). */
    public static final String BATTERY_FULL_SMALL = "FontAwesome.battery-full";
    /** the name of the large(default: 24x24 px) battery-full image (used for toolbar buttons or icons). */
    public static final String BATTERY_FULL_LARGE = "FontAwesome.battery-full;size=24";

    /** the name of the small(default: 16x16 px) battery-3 image (used for menuitems or buttons). */
    public static final String BATTERY_3_SMALL = "FontAwesome.battery-3";
    /** the name of the large(default: 24x24 px) battery-3 image (used for toolbar buttons or icons). */
    public static final String BATTERY_3_LARGE = "FontAwesome.battery-3;size=24";

    /** the name of the small(default: 16x16 px) battery-three-quarters image (used for menuitems or buttons). */
    public static final String BATTERY_THREE_QUARTERS_SMALL = "FontAwesome.battery-three-quarters";
    /** the name of the large(default: 24x24 px) battery-three-quarters image (used for toolbar buttons or icons). */
    public static final String BATTERY_THREE_QUARTERS_LARGE = "FontAwesome.battery-three-quarters;size=24";

    /** the name of the small(default: 16x16 px) battery-2 image (used for menuitems or buttons). */
    public static final String BATTERY_2_SMALL = "FontAwesome.battery-2";
    /** the name of the large(default: 24x24 px) battery-2 image (used for toolbar buttons or icons). */
    public static final String BATTERY_2_LARGE = "FontAwesome.battery-2;size=24";

    /** the name of the small(default: 16x16 px) battery-half image (used for menuitems or buttons). */
    public static final String BATTERY_HALF_SMALL = "FontAwesome.battery-half";
    /** the name of the large(default: 24x24 px) battery-half image (used for toolbar buttons or icons). */
    public static final String BATTERY_HALF_LARGE = "FontAwesome.battery-half;size=24";

    /** the name of the small(default: 16x16 px) battery-1 image (used for menuitems or buttons). */
    public static final String BATTERY_1_SMALL = "FontAwesome.battery-1";
    /** the name of the large(default: 24x24 px) battery-1 image (used for toolbar buttons or icons). */
    public static final String BATTERY_1_LARGE = "FontAwesome.battery-1;size=24";

    /** the name of the small(default: 16x16 px) battery-quarter image (used for menuitems or buttons). */
    public static final String BATTERY_QUARTER_SMALL = "FontAwesome.battery-quarter";
    /** the name of the large(default: 24x24 px) battery-quarter image (used for toolbar buttons or icons). */
    public static final String BATTERY_QUARTER_LARGE = "FontAwesome.battery-quarter;size=24";

    /** the name of the small(default: 16x16 px) battery-0 image (used for menuitems or buttons). */
    public static final String BATTERY_0_SMALL = "FontAwesome.battery-0";
    /** the name of the large(default: 24x24 px) battery-0 image (used for toolbar buttons or icons). */
    public static final String BATTERY_0_LARGE = "FontAwesome.battery-0;size=24";

    /** the name of the small(default: 16x16 px) battery-empty image (used for menuitems or buttons). */
    public static final String BATTERY_EMPTY_SMALL = "FontAwesome.battery-empty";
    /** the name of the large(default: 24x24 px) battery-empty image (used for toolbar buttons or icons). */
    public static final String BATTERY_EMPTY_LARGE = "FontAwesome.battery-empty;size=24";

    /** the name of the small(default: 16x16 px) mouse-pointer image (used for menuitems or buttons). */
    public static final String MOUSE_POINTER_SMALL = "FontAwesome.mouse-pointer";
    /** the name of the large(default: 24x24 px) mouse-pointer image (used for toolbar buttons or icons). */
    public static final String MOUSE_POINTER_LARGE = "FontAwesome.mouse-pointer;size=24";

    /** the name of the small(default: 16x16 px) i-cursor image (used for menuitems or buttons). */
    public static final String I_CURSOR_SMALL = "FontAwesome.i-cursor";
    /** the name of the large(default: 24x24 px) i-cursor image (used for toolbar buttons or icons). */
    public static final String I_CURSOR_LARGE = "FontAwesome.i-cursor;size=24";

    /** the name of the small(default: 16x16 px) object-group image (used for menuitems or buttons). */
    public static final String OBJECT_GROUP_SMALL = "FontAwesome.object-group";
    /** the name of the large(default: 24x24 px) object-group image (used for toolbar buttons or icons). */
    public static final String OBJECT_GROUP_LARGE = "FontAwesome.object-group;size=24";

    /** the name of the small(default: 16x16 px) object-ungroup image (used for menuitems or buttons). */
    public static final String OBJECT_UNGROUP_SMALL = "FontAwesome.object-ungroup";
    /** the name of the large(default: 24x24 px) object-ungroup image (used for toolbar buttons or icons). */
    public static final String OBJECT_UNGROUP_LARGE = "FontAwesome.object-ungroup;size=24";

    /** the name of the small(default: 16x16 px) sticky-note image (used for menuitems or buttons). */
    public static final String STICKY_NOTE_SMALL = "FontAwesome.sticky-note";
    /** the name of the large(default: 24x24 px) sticky-note image (used for toolbar buttons or icons). */
    public static final String STICKY_NOTE_LARGE = "FontAwesome.sticky-note;size=24";

    /** the name of the small(default: 16x16 px) sticky-note-o image (used for menuitems or buttons). */
    public static final String STICKY_NOTE_O_SMALL = "FontAwesome.sticky-note-o";
    /** the name of the large(default: 24x24 px) sticky-note-o image (used for toolbar buttons or icons). */
    public static final String STICKY_NOTE_O_LARGE = "FontAwesome.sticky-note-o;size=24";

    /** the name of the small(default: 16x16 px) cc-jcb image (used for menuitems or buttons). */
    public static final String CC_JCB_SMALL = "FontAwesome.cc-jcb";
    /** the name of the large(default: 24x24 px) cc-jcb image (used for toolbar buttons or icons). */
    public static final String CC_JCB_LARGE = "FontAwesome.cc-jcb;size=24";

    /** the name of the small(default: 16x16 px) cc-diners-club image (used for menuitems or buttons). */
    public static final String CC_DINERS_CLUB_SMALL = "FontAwesome.cc-diners-club";
    /** the name of the large(default: 24x24 px) cc-diners-club image (used for toolbar buttons or icons). */
    public static final String CC_DINERS_CLUB_LARGE = "FontAwesome.cc-diners-club;size=24";

    /** the name of the small(default: 16x16 px) clone image (used for menuitems or buttons). */
    public static final String CLONE_SMALL = "FontAwesome.clone";
    /** the name of the large(default: 24x24 px) clone image (used for toolbar buttons or icons). */
    public static final String CLONE_LARGE = "FontAwesome.clone;size=24";

    /** the name of the small(default: 16x16 px) balance-scale image (used for menuitems or buttons). */
    public static final String BALANCE_SCALE_SMALL = "FontAwesome.balance-scale";
    /** the name of the large(default: 24x24 px) balance-scale image (used for toolbar buttons or icons). */
    public static final String BALANCE_SCALE_LARGE = "FontAwesome.balance-scale;size=24";

    /** the name of the small(default: 16x16 px) hourglass-o image (used for menuitems or buttons). */
    public static final String HOURGLASS_O_SMALL = "FontAwesome.hourglass-o";
    /** the name of the large(default: 24x24 px) hourglass-o image (used for toolbar buttons or icons). */
    public static final String HOURGLASS_O_LARGE = "FontAwesome.hourglass-o;size=24";

    /** the name of the small(default: 16x16 px) hourglass-1 image (used for menuitems or buttons). */
    public static final String HOURGLASS_1_SMALL = "FontAwesome.hourglass-1";
    /** the name of the large(default: 24x24 px) hourglass-1 image (used for toolbar buttons or icons). */
    public static final String HOURGLASS_1_LARGE = "FontAwesome.hourglass-1;size=24";

    /** the name of the small(default: 16x16 px) hourglass-start image (used for menuitems or buttons). */
    public static final String HOURGLASS_START_SMALL = "FontAwesome.hourglass-start";
    /** the name of the large(default: 24x24 px) hourglass-start image (used for toolbar buttons or icons). */
    public static final String HOURGLASS_START_LARGE = "FontAwesome.hourglass-start;size=24";

    /** the name of the small(default: 16x16 px) hourglass-2 image (used for menuitems or buttons). */
    public static final String HOURGLASS_2_SMALL = "FontAwesome.hourglass-2";
    /** the name of the large(default: 24x24 px) hourglass-2 image (used for toolbar buttons or icons). */
    public static final String HOURGLASS_2_LARGE = "FontAwesome.hourglass-2;size=24";

    /** the name of the small(default: 16x16 px) hourglass-half image (used for menuitems or buttons). */
    public static final String HOURGLASS_HALF_SMALL = "FontAwesome.hourglass-half";
    /** the name of the large(default: 24x24 px) hourglass-half image (used for toolbar buttons or icons). */
    public static final String HOURGLASS_HALF_LARGE = "FontAwesome.hourglass-half;size=24";

    /** the name of the small(default: 16x16 px) hourglass-3 image (used for menuitems or buttons). */
    public static final String HOURGLASS_3_SMALL = "FontAwesome.hourglass-3";
    /** the name of the large(default: 24x24 px) hourglass-3 image (used for toolbar buttons or icons). */
    public static final String HOURGLASS_3_LARGE = "FontAwesome.hourglass-3;size=24";

    /** the name of the small(default: 16x16 px) hourglass-end image (used for menuitems or buttons). */
    public static final String HOURGLASS_END_SMALL = "FontAwesome.hourglass-end";
    /** the name of the large(default: 24x24 px) hourglass-end image (used for toolbar buttons or icons). */
    public static final String HOURGLASS_END_LARGE = "FontAwesome.hourglass-end;size=24";

    /** the name of the small(default: 16x16 px) hourglass image (used for menuitems or buttons). */
    public static final String HOURGLASS_SMALL = "FontAwesome.hourglass";
    /** the name of the large(default: 24x24 px) hourglass image (used for toolbar buttons or icons). */
    public static final String HOURGLASS_LARGE = "FontAwesome.hourglass;size=24";

    /** the name of the small(default: 16x16 px) hand-grab-o image (used for menuitems or buttons). */
    public static final String HAND_GRAB_O_SMALL = "FontAwesome.hand-grab-o";
    /** the name of the large(default: 24x24 px) hand-grab-o image (used for toolbar buttons or icons). */
    public static final String HAND_GRAB_O_LARGE = "FontAwesome.hand-grab-o;size=24";

    /** the name of the small(default: 16x16 px) hand-rock-o image (used for menuitems or buttons). */
    public static final String HAND_ROCK_O_SMALL = "FontAwesome.hand-rock-o";
    /** the name of the large(default: 24x24 px) hand-rock-o image (used for toolbar buttons or icons). */
    public static final String HAND_ROCK_O_LARGE = "FontAwesome.hand-rock-o;size=24";

    /** the name of the small(default: 16x16 px) hand-stop-o image (used for menuitems or buttons). */
    public static final String HAND_STOP_O_SMALL = "FontAwesome.hand-stop-o";
    /** the name of the large(default: 24x24 px) hand-stop-o image (used for toolbar buttons or icons). */
    public static final String HAND_STOP_O_LARGE = "FontAwesome.hand-stop-o;size=24";

    /** the name of the small(default: 16x16 px) hand-paper-o image (used for menuitems or buttons). */
    public static final String HAND_PAPER_O_SMALL = "FontAwesome.hand-paper-o";
    /** the name of the large(default: 24x24 px) hand-paper-o image (used for toolbar buttons or icons). */
    public static final String HAND_PAPER_O_LARGE = "FontAwesome.hand-paper-o;size=24";

    /** the name of the small(default: 16x16 px) hand-scissors-o image (used for menuitems or buttons). */
    public static final String HAND_SCISSORS_O_SMALL = "FontAwesome.hand-scissors-o";
    /** the name of the large(default: 24x24 px) hand-scissors-o image (used for toolbar buttons or icons). */
    public static final String HAND_SCISSORS_O_LARGE = "FontAwesome.hand-scissors-o;size=24";

    /** the name of the small(default: 16x16 px) hand-lizard-o image (used for menuitems or buttons). */
    public static final String HAND_LIZARD_O_SMALL = "FontAwesome.hand-lizard-o";
    /** the name of the large(default: 24x24 px) hand-lizard-o image (used for toolbar buttons or icons). */
    public static final String HAND_LIZARD_O_LARGE = "FontAwesome.hand-lizard-o;size=24";

    /** the name of the small(default: 16x16 px) hand-spock-o image (used for menuitems or buttons). */
    public static final String HAND_SPOCK_O_SMALL = "FontAwesome.hand-spock-o";
    /** the name of the large(default: 24x24 px) hand-spock-o image (used for toolbar buttons or icons). */
    public static final String HAND_SPOCK_O_LARGE = "FontAwesome.hand-spock-o;size=24";

    /** the name of the small(default: 16x16 px) hand-pointer-o image (used for menuitems or buttons). */
    public static final String HAND_POINTER_O_SMALL = "FontAwesome.hand-pointer-o";
    /** the name of the large(default: 24x24 px) hand-pointer-o image (used for toolbar buttons or icons). */
    public static final String HAND_POINTER_O_LARGE = "FontAwesome.hand-pointer-o;size=24";

    /** the name of the small(default: 16x16 px) hand-peace-o image (used for menuitems or buttons). */
    public static final String HAND_PEACE_O_SMALL = "FontAwesome.hand-peace-o";
    /** the name of the large(default: 24x24 px) hand-peace-o image (used for toolbar buttons or icons). */
    public static final String HAND_PEACE_O_LARGE = "FontAwesome.hand-peace-o;size=24";

    /** the name of the small(default: 16x16 px) trademark image (used for menuitems or buttons). */
    public static final String TRADEMARK_SMALL = "FontAwesome.trademark";
    /** the name of the large(default: 24x24 px) trademark image (used for toolbar buttons or icons). */
    public static final String TRADEMARK_LARGE = "FontAwesome.trademark;size=24";

    /** the name of the small(default: 16x16 px) registered image (used for menuitems or buttons). */
    public static final String REGISTERED_SMALL = "FontAwesome.registered";
    /** the name of the large(default: 24x24 px) registered image (used for toolbar buttons or icons). */
    public static final String REGISTERED_LARGE = "FontAwesome.registered;size=24";

    /** the name of the small(default: 16x16 px) creative-commons image (used for menuitems or buttons). */
    public static final String CREATIVE_COMMONS_SMALL = "FontAwesome.creative-commons";
    /** the name of the large(default: 24x24 px) creative-commons image (used for toolbar buttons or icons). */
    public static final String CREATIVE_COMMONS_LARGE = "FontAwesome.creative-commons;size=24";

    /** the name of the small(default: 16x16 px) gg image (used for menuitems or buttons). */
    public static final String GG_SMALL = "FontAwesome.gg";
    /** the name of the large(default: 24x24 px) gg image (used for toolbar buttons or icons). */
    public static final String GG_LARGE = "FontAwesome.gg;size=24";

    /** the name of the small(default: 16x16 px) gg-circle image (used for menuitems or buttons). */
    public static final String GG_CIRCLE_SMALL = "FontAwesome.gg-circle";
    /** the name of the large(default: 24x24 px) gg-circle image (used for toolbar buttons or icons). */
    public static final String GG_CIRCLE_LARGE = "FontAwesome.gg-circle;size=24";

    /** the name of the small(default: 16x16 px) tripadvisor image (used for menuitems or buttons). */
    public static final String TRIPADVISOR_SMALL = "FontAwesome.tripadvisor";
    /** the name of the large(default: 24x24 px) tripadvisor image (used for toolbar buttons or icons). */
    public static final String TRIPADVISOR_LARGE = "FontAwesome.tripadvisor;size=24";

    /** the name of the small(default: 16x16 px) odnoklassniki image (used for menuitems or buttons). */
    public static final String ODNOKLASSNIKI_SMALL = "FontAwesome.odnoklassniki";
    /** the name of the large(default: 24x24 px) odnoklassniki image (used for toolbar buttons or icons). */
    public static final String ODNOKLASSNIKI_LARGE = "FontAwesome.odnoklassniki;size=24";

    /** the name of the small(default: 16x16 px) odnoklassniki-square image (used for menuitems or buttons). */
    public static final String ODNOKLASSNIKI_SQUARE_SMALL = "FontAwesome.odnoklassniki-square";
    /** the name of the large(default: 24x24 px) odnoklassniki-square image (used for toolbar buttons or icons). */
    public static final String ODNOKLASSNIKI_SQUARE_LARGE = "FontAwesome.odnoklassniki-square;size=24";

    /** the name of the small(default: 16x16 px) get-pocket image (used for menuitems or buttons). */
    public static final String GET_POCKET_SMALL = "FontAwesome.get-pocket";
    /** the name of the large(default: 24x24 px) get-pocket image (used for toolbar buttons or icons). */
    public static final String GET_POCKET_LARGE = "FontAwesome.get-pocket;size=24";

    /** the name of the small(default: 16x16 px) wikipedia-w image (used for menuitems or buttons). */
    public static final String WIKIPEDIA_W_SMALL = "FontAwesome.wikipedia-w";
    /** the name of the large(default: 24x24 px) wikipedia-w image (used for toolbar buttons or icons). */
    public static final String WIKIPEDIA_W_LARGE = "FontAwesome.wikipedia-w;size=24";

    /** the name of the small(default: 16x16 px) safari image (used for menuitems or buttons). */
    public static final String SAFARI_SMALL = "FontAwesome.safari";
    /** the name of the large(default: 24x24 px) safari image (used for toolbar buttons or icons). */
    public static final String SAFARI_LARGE = "FontAwesome.safari;size=24";

    /** the name of the small(default: 16x16 px) chrome image (used for menuitems or buttons). */
    public static final String CHROME_SMALL = "FontAwesome.chrome";
    /** the name of the large(default: 24x24 px) chrome image (used for toolbar buttons or icons). */
    public static final String CHROME_LARGE = "FontAwesome.chrome;size=24";

    /** the name of the small(default: 16x16 px) firefox image (used for menuitems or buttons). */
    public static final String FIREFOX_SMALL = "FontAwesome.firefox";
    /** the name of the large(default: 24x24 px) firefox image (used for toolbar buttons or icons). */
    public static final String FIREFOX_LARGE = "FontAwesome.firefox;size=24";

    /** the name of the small(default: 16x16 px) opera image (used for menuitems or buttons). */
    public static final String OPERA_SMALL = "FontAwesome.opera";
    /** the name of the large(default: 24x24 px) opera image (used for toolbar buttons or icons). */
    public static final String OPERA_LARGE = "FontAwesome.opera;size=24";

    /** the name of the small(default: 16x16 px) internet-explorer image (used for menuitems or buttons). */
    public static final String INTERNET_EXPLORER_SMALL = "FontAwesome.internet-explorer";
    /** the name of the large(default: 24x24 px) internet-explorer image (used for toolbar buttons or icons). */
    public static final String INTERNET_EXPLORER_LARGE = "FontAwesome.internet-explorer;size=24";

    /** the name of the small(default: 16x16 px) tv image (used for menuitems or buttons). */
    public static final String TV_SMALL = "FontAwesome.tv";
    /** the name of the large(default: 24x24 px) tv image (used for toolbar buttons or icons). */
    public static final String TV_LARGE = "FontAwesome.tv;size=24";

    /** the name of the small(default: 16x16 px) television image (used for menuitems or buttons). */
    public static final String TELEVISION_SMALL = "FontAwesome.television";
    /** the name of the large(default: 24x24 px) television image (used for toolbar buttons or icons). */
    public static final String TELEVISION_LARGE = "FontAwesome.television;size=24";

    /** the name of the small(default: 16x16 px) contao image (used for menuitems or buttons). */
    public static final String CONTAO_SMALL = "FontAwesome.contao";
    /** the name of the large(default: 24x24 px) contao image (used for toolbar buttons or icons). */
    public static final String CONTAO_LARGE = "FontAwesome.contao;size=24";

    /** the name of the small(default: 16x16 px) 500px image (used for menuitems or buttons). */
    public static final String FIVEHUNDRED_PX_SMALL = "FontAwesome.500px";
    /** the name of the large(default: 24x24 px) 500px image (used for toolbar buttons or icons). */
    public static final String FIVEHUNDRED_PX_LARGE = "FontAwesome.500px;size=24";

    /** the name of the small(default: 16x16 px) amazon image (used for menuitems or buttons). */
    public static final String AMAZON_SMALL = "FontAwesome.amazon";
    /** the name of the large(default: 24x24 px) amazon image (used for toolbar buttons or icons). */
    public static final String AMAZON_LARGE = "FontAwesome.amazon;size=24";

    /** the name of the small(default: 16x16 px) calendar-plus-o image (used for menuitems or buttons). */
    public static final String CALENDAR_PLUS_O_SMALL = "FontAwesome.calendar-plus-o";
    /** the name of the large(default: 24x24 px) calendar-plus-o image (used for toolbar buttons or icons). */
    public static final String CALENDAR_PLUS_O_LARGE = "FontAwesome.calendar-plus-o;size=24";

    /** the name of the small(default: 16x16 px) calendar-minus-o image (used for menuitems or buttons). */
    public static final String CALENDAR_MINUS_O_SMALL = "FontAwesome.calendar-minus-o";
    /** the name of the large(default: 24x24 px) calendar-minus-o image (used for toolbar buttons or icons). */
    public static final String CALENDAR_MINUS_O_LARGE = "FontAwesome.calendar-minus-o;size=24";

    /** the name of the small(default: 16x16 px) calendar-times-o image (used for menuitems or buttons). */
    public static final String CALENDAR_TIMES_O_SMALL = "FontAwesome.calendar-times-o";
    /** the name of the large(default: 24x24 px) calendar-times-o image (used for toolbar buttons or icons). */
    public static final String CALENDAR_TIMES_O_LARGE = "FontAwesome.calendar-times-o;size=24";

    /** the name of the small(default: 16x16 px) calendar-check-o image (used for menuitems or buttons). */
    public static final String CALENDAR_CHECK_O_SMALL = "FontAwesome.calendar-check-o";
    /** the name of the large(default: 24x24 px) calendar-check-o image (used for toolbar buttons or icons). */
    public static final String CALENDAR_CHECK_O_LARGE = "FontAwesome.calendar-check-o;size=24";

    /** the name of the small(default: 16x16 px) industry image (used for menuitems or buttons). */
    public static final String INDUSTRY_SMALL = "FontAwesome.industry";
    /** the name of the large(default: 24x24 px) industry image (used for toolbar buttons or icons). */
    public static final String INDUSTRY_LARGE = "FontAwesome.industry;size=24";

    /** the name of the small(default: 16x16 px) map-pin image (used for menuitems or buttons). */
    public static final String MAP_PIN_SMALL = "FontAwesome.map-pin";
    /** the name of the large(default: 24x24 px) map-pin image (used for toolbar buttons or icons). */
    public static final String MAP_PIN_LARGE = "FontAwesome.map-pin;size=24";

    /** the name of the small(default: 16x16 px) map-signs image (used for menuitems or buttons). */
    public static final String MAP_SIGNS_SMALL = "FontAwesome.map-signs";
    /** the name of the large(default: 24x24 px) map-signs image (used for toolbar buttons or icons). */
    public static final String MAP_SIGNS_LARGE = "FontAwesome.map-signs;size=24";

    /** the name of the small(default: 16x16 px) map-o image (used for menuitems or buttons). */
    public static final String MAP_O_SMALL = "FontAwesome.map-o";
    /** the name of the large(default: 24x24 px) map-o image (used for toolbar buttons or icons). */
    public static final String MAP_O_LARGE = "FontAwesome.map-o;size=24";

    /** the name of the small(default: 16x16 px) map image (used for menuitems or buttons). */
    public static final String MAP_SMALL = "FontAwesome.map";
    /** the name of the large(default: 24x24 px) map image (used for toolbar buttons or icons). */
    public static final String MAP_LARGE = "FontAwesome.map;size=24";

    /** the name of the small(default: 16x16 px) commenting image (used for menuitems or buttons). */
    public static final String COMMENTING_SMALL = "FontAwesome.commenting";
    /** the name of the large(default: 24x24 px) commenting image (used for toolbar buttons or icons). */
    public static final String COMMENTING_LARGE = "FontAwesome.commenting;size=24";

    /** the name of the small(default: 16x16 px) commenting-o image (used for menuitems or buttons). */
    public static final String COMMENTING_O_SMALL = "FontAwesome.commenting-o";
    /** the name of the large(default: 24x24 px) commenting-o image (used for toolbar buttons or icons). */
    public static final String COMMENTING_O_LARGE = "FontAwesome.commenting-o;size=24";

    /** the name of the small(default: 16x16 px) houzz image (used for menuitems or buttons). */
    public static final String HOUZZ_SMALL = "FontAwesome.houzz";
    /** the name of the large(default: 24x24 px) houzz image (used for toolbar buttons or icons). */
    public static final String HOUZZ_LARGE = "FontAwesome.houzz;size=24";

    /** the name of the small(default: 16x16 px) vimeo image (used for menuitems or buttons). */
    public static final String VIMEO_SMALL = "FontAwesome.vimeo";
    /** the name of the large(default: 24x24 px) vimeo image (used for toolbar buttons or icons). */
    public static final String VIMEO_LARGE = "FontAwesome.vimeo;size=24";

    /** the name of the small(default: 16x16 px) black-tie image (used for menuitems or buttons). */
    public static final String BLACK_TIE_SMALL = "FontAwesome.black-tie";
    /** the name of the large(default: 24x24 px) black-tie image (used for toolbar buttons or icons). */
    public static final String BLACK_TIE_LARGE = "FontAwesome.black-tie;size=24";

    /** the name of the small(default: 16x16 px) fonticons image (used for menuitems or buttons). */
    public static final String FONTICONS_SMALL = "FontAwesome.fonticons";
    /** the name of the large(default: 24x24 px) fonticons image (used for toolbar buttons or icons). */
    public static final String FONTICONS_LARGE = "FontAwesome.fonticons;size=24";
    
}   // IFontAwesome
