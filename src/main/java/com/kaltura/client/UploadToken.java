// ===================================================================================================
//                           _  __     _ _
//                          | |/ /__ _| | |_ _  _ _ _ __ _
//                          | ' </ _` | |  _| || | '_/ _` |
//                          |_|\_\__,_|_|\__|\_,_|_| \__,_|
//
// This file is part of the Kaltura Collaborative Media Suite which allows users
// to do with audio, video, and animation what Wiki platfroms allow them to do with
// text.
//
// Copyright (C) 2006-2011  Kaltura Inc.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
// @ignore
// ===================================================================================================
package com.kaltura.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.types.KalturaMediaEntry;
import com.kaltura.client.types.KalturaUploadToken;
import com.kaltura.client.types.KalturaUploadedFileTokenResource;

import com.kaltura.client.IKalturaLogger;
import com.kaltura.client.KalturaLogger;

/**
 * Upload files to the server
 */
public class UploadToken {
    
    private String TAG;
    private File fileData;
    private KalturaUploadToken kalturaUploadToken;
    private KalturaClient client;
    private int setAttemptUpload;
    private KalturaMediaEntry newEntry;
    private double remainingUploadFileSize;
    private boolean startUpload;
    private int readSum = 0;
    
    private IKalturaLogger log = KalturaLogger.getLogger(getClass()); 
    
    /**
     * Constructor Description of UploadToken
     *
     * @param TAG constant in your class
     * @param setAttemptUpload quantity attempts upload to the server
     */
    public UploadToken(String TAG, int setAttemptUpload, KalturaClient _client) {
        this.TAG = TAG;
        fileData = new File(TAG);
        this.setAttemptUpload = setAttemptUpload;
        kalturaUploadToken = new KalturaUploadToken();
        remainingUploadFileSize = kalturaUploadToken.uploadedFileSize;
        client = _client;
        
    }
    
    /**
     * Get size uploading file in percent
     *
     * @return size uploading file in percent
     */
    public int getUploadedFileSize() {
        int res = 0;
        try {
            res = (int) (kalturaUploadToken.uploadedFileSize / fileData.length() * 100.0);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            log.error(e);
            res = 0;
        }
        return res;
    }
    
    public void setStartUpload(boolean startUpload) {
        this.startUpload = startUpload;
    }
    
    /**
     * uploads a video file to Kaltura and assigns it to a given Media Entry
     * object
     *
     * @param TAG constant in your class
     * @param entry Entry to which is attached to the file
     * @param pathfromURI File path on local storage
     *
     * @return true - file uploaded; false - file not uploaded
     *
     */
    public boolean uploadMediaFileAndAttachToEmptyEntry(String TAG, KalturaMediaEntry entry, String pathfromURI) {
        log.error("\nUploading a video file...");
        readSum = 0;
        fileData = new File(pathfromURI);
        
        KalturaUploadToken upToken = null;
        try {
            upToken = client.getUploadTokenService().add();
        } catch (KalturaApiException ex) {
            log.error(ex);
        }
        
        int sizeBuf = 1024 * 10000;
        byte buf[] = new byte[sizeBuf];
        int numRead = -1;
        String PATH = "/tmp/";
        File outFile = null;
        FileOutputStream fos = null;
        FileInputStream fis = null;
        boolean uploaded = false;
        try {
            fis = new FileInputStream(pathfromURI);
        } catch (FileNotFoundException ex) {
            log.error("err: ", ex);
        }
        int i = 0;
        boolean errUpload = false;
        log.error("HASH:" + new Float(fileData.length()).hashCode());
        int attemptUpload = 0;
        
        boolean wasFirst = false;
        do {
            if (!errUpload) {
                try {
                    log.error("Available bytes: " + fis.available());
                    remainingUploadFileSize = fis.available();
                    if(remainingUploadFileSize == 0){
                        uploaded = true;
                        break;
                    }
                    buf = new byte[sizeBuf];
                    numRead = fis.read(buf);
                    log.error("Readed bytes: " + numRead);
                    outFile = new File(PATH, "upload.dat");
                    fos = new FileOutputStream(outFile);
                    fos.write(buf, 0, numRead);
                    fos.close();
                } catch (IOException ex) {
                    log.error("err: ", ex);
                }
            } else {
                attemptUpload++;
                log.error("upload error chunk: attemptUpload - " + attemptUpload);
                if (attemptUpload >= setAttemptUpload) {
                    log.error("---");
                }
                
            }
            if (fileData.length() > sizeBuf) {
                if (remainingUploadFileSize > sizeBuf) {
                    if (addChunk(client, upToken.id, outFile, readSum != 0, false, readSum)) {
                        log.error("1 chunk[" + ++i + "] - uploaddFileSize: " + kalturaUploadToken.uploadedFileSize);
                        wasFirst = true;
                        readSum += numRead;
                    } else {
                        log.error("error loading chunk!");
                        errUpload = true;
                    }
                } else {
                    if (addChunk(client, upToken.id, outFile, true, true, readSum)) {
                        log.error("n chunk[" + ++i + "] - uploaddFileSize: " + kalturaUploadToken.uploadedFileSize);
                        readSum += numRead;
                        uploaded = true;
                    } else {
                        log.error("error loading chunk!");
                        errUpload = true;
                    }
                }
            } else {
                log.error("was:" + wasFirst);
                if (wasFirst) {
                    if (addChunk(client, upToken.id, outFile, true, true, -1)) {
                        log.error("l chunk[" + ++i + "] - uploaddFileSize: " + kalturaUploadToken.uploadedFileSize);
                        uploaded = true;
                        wasFirst = false;
                    } else {
                        log.error("error loading chunk!");
                        errUpload = true;
                    }
                } else {
                    if (addChunk(client, upToken.id, outFile, false, true, -1)) {
                        log.error("n chunk[" + ++i + "] - uploaddFileSize: " + kalturaUploadToken.uploadedFileSize);
                        uploaded = true;
                        wasFirst = false;
                    } else {
                        log.error("error loading chunk!");
                        errUpload = true;
                    }
                }
            }            
            log.error("uploaded " + getUploadedFileSize() + "%");
        } while (!uploaded && !(attemptUpload >= setAttemptUpload) && startUpload);
        
        log.error("HASH:" + new Float(kalturaUploadToken.uploadedFileSize).hashCode());
        if (uploaded) {
            startUpload = false;
            try {
                KalturaUploadedFileTokenResource fileTokenResource = new KalturaUploadedFileTokenResource();
                fileTokenResource.token = upToken.id;
                newEntry = client.getMediaService().addContent(entry.id, fileTokenResource);
                
                log.error("\nUploaded a new Video file to entry: " + newEntry.id);
            } catch (KalturaApiException e) {
                e.printStackTrace();
                log.error("err: " + e.getMessage());
            }
        } else {
            uploaded = false;
        }
        try {
            fis.close();
            fos.close();
        } catch (IOException ex) {
            log.error("err: ", ex);
        }
        return uploaded;
    }
    
    /**
     * @param KalturaClient client
     * @param String uploadTokenId
     * @param File outFile
     * @param int i
     *
     * @return
     */
    private boolean addChunk(KalturaClient client, String uploadTokenId, File outFile, boolean resume, boolean finalChunk, int resumeAt) {
        boolean isUploaded = false;
        try {
            kalturaUploadToken = client.getUploadTokenService().upload(uploadTokenId, outFile, resume, finalChunk, resumeAt);
            outFile.delete();
            isUploaded = true;
        } catch (KalturaApiException e) {
            e.printStackTrace();
            isUploaded = false;
            log.error("err: " + e.getMessage());
        }
        return isUploaded;
    }
}