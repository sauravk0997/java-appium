package com.disney.alice.labels;

public enum AliceLabels {

    ACTIVATION_CODE("activation_code"),
    BANNER("banner"),
    BANNER_HOVERED("banner_hovered"),
    BUTTON("button"),
    BUTTON_HOVERED("button_hovered"),
    BUTTON_SHOW_PASSWORD("button_show_password"),
    CELL_PHONE_IMAGE("cell_phone_image"),
    DARTH_MAUL_AVATAR("darth_maul_avatar"),
    DESCRIPTION("description"),
    DISNEY_BRAND_TILE("disney_brand_tile"),
    DISNEY_LOGO("disney_logo"),
    ERROR_MESSAGE("error_message"),
    ERROR_TEXT_FIELD("error_text_field"),
    ERROR_TEXT_FIELD_HOVERED("error_text_field_hovered"),
    HOME_BUTTON_IS_HOVERED("home_button_hovered"),
    HOME_BUTTON_IS_SELECTED("home_button_selected"),
    LOADING_ANIMATION("loading_animation"),
    MARVEL_LOGO("marvel_logo"),
    MICKEY_MOUSE_AVATAR("mickey_mouse_avatar"),
    MOVIES_ICON("reel_button"),
    NAT_GEO_LOGO("natgeo_logo"),
    ORIGINALS_ICON("star_button"),
    PASSWORD_STRENGTH_BAR("password_strength_bar"),
    PIXAR_LOGO("pixar_logo"),
    PROFILE_BUTTON("profile_button"),
    PROFILE_BUTTON_HOVERED("profile_button_hovered"),
    RECTANGLE_TILE("rectangle_tile"),
    ROUND_TILE("round_tile"),
    ROUND_TILE_HOVERED("round_tile_hovered"),
    SEARCH_ICON("search_icon"),
    SERIES_ICON("tv_button"),
    SETTINGS_BUTTON_IS_HOVERED("settings_button_hovered"),
    SETTINGS_ICON("settings_button"),
    STAR_WARS_LOGO("star_wars_logo"),
    TITLE("title"),
    VERTICAL_MENU_ITEM("vertical_menu_item"),
    VERTICAL_MENU_ITEM_HOVERED("vertical_menu_item_hovered"),
    VERTICAL_MENU_ITEM_SELECTED("vertical_menu_item_selected"),
    VERTICAL_MENU_ITEM_HOVERED_ROUND("vertical_menu_item_hovered_round"),
    VERTICAL_MENU_ITEM_HOVERED_VERT_SEPARATOR("vertical_menu_item_hovered_vert_separator"),
    WATCHLIST_ICON("plus_button"),
    CLOSED_CAPTIONING("closed_captioning");

    private final String label;

    AliceLabels(String label) {
        this.label = label;
    }

    public String getText() {
        return label;
    }
}

