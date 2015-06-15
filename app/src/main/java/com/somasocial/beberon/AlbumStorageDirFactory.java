package com.somasocial.beberon;

/**
 * Created by SOMA on 15/06/15.
 */

import java.io.File;

abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}

