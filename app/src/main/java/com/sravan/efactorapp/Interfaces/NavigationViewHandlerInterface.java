package com.sravan.efactorapp.Interfaces;

import android.graphics.drawable.Drawable;

public interface NavigationViewHandlerInterface {
    void setNavTitle(String title);


    void setNavigationToolbarVisibilty(boolean visibilty);
    void setNavToggleButtonVisibilty(boolean visibilty);
    void setBackButtonVisibilty(boolean visibilty);

    void setHeaderProfilePic(String uri, Drawable drawable);

    void setUserName(String Name);


    /*for home*/
    void setLocationTitle(String Name);
    void setCurrentLocation(String Name);
    void setLocationComponentVisibilty(boolean visibilty);

    void lockDrawer(boolean visibilty);

    void setTitleButtonVisibility(boolean visibilty);
}
