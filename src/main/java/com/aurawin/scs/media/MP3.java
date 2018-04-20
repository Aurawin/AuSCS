package com.aurawin.scs.media;

import com.aurawin.core.stream.MemoryStream;
import com.aurawin.mp3.Details;
import com.aurawin.mp3.frame.Frame;
import com.aurawin.mp3.frame.Reader;
import com.aurawin.mp3.frame.tag.Event;


public class MP3 {

    public static Details Load(MemoryStream Data){
        Details Deets = new Details();

        Reader id3 = new Reader();

        id3.setTagFrameEvent(
            new Event() {
                @Override
                public void Handle(Frame Main, Frame TagFrame) {
                    switch(TagFrame.Kind){
                        case fTitle:
                            Deets.Title = TagFrame.Payload.pldText.Data;
                            break;
                        case fTitleDescription :
                            Deets.Description = TagFrame.Payload.pldText.Data;
                            break;
                        case fLeadArtist:
                            Deets.Artist = TagFrame.Payload.pldText.Data;
                            break;
                        case fTrackNumber:
                            Deets.Track = TagFrame.Payload.pldText.Data;
                            break;
                        case fYear:
                            Deets.Year = TagFrame.Payload.pldText.Data;
                            break;
                        case fContentType:
                            Deets.ContentType = TagFrame.Payload.pldText.Data;
                            break;
                        case fComments:
                            Deets.Comment = TagFrame.Payload.pldCOM.Description;
                            break;
                        case fEncodedBy:
                            Deets.Encoder = TagFrame.Payload.pldText.Data;
                            break;
                        case fLength:
                            Deets.Duration = Long.parseLong(TagFrame.Payload.pldText.Data);
                            break;
                        case fUniqueFileID:
                            Deets.UniqueFileId = TagFrame.Payload.pldUFI.Owner;
                            break;

                    }
                }
            }
        );
        try  {
            id3.Load(Data);

        } catch (Exception e){

        }


        return Deets;

    }

}
