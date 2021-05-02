package co.messagesblockchain.app.controllers;

import co.messagesblockchain.app.model.Block;
import co.messagesblockchain.app.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class MessagesController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(method = RequestMethod.GET, path = {"", "/"})
    public ResponseEntity<List<Block>> getMessageBlocks(){
        List<Block> blocks = messageService.getMessages();

        return new ResponseEntity<>(blocks, HttpStatus.ACCEPTED);
    }

}
