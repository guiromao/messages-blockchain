package co.messagesblockchain.app.controllers;

import co.messagesblockchain.app.dto.MessageDto;
import co.messagesblockchain.app.dto.ValidDto;
import co.messagesblockchain.app.model.Block;
import co.messagesblockchain.app.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MessagesController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(method = RequestMethod.GET, path = {"", "/"})
    public ResponseEntity<List<Block>> getMessageBlocks(){
        List<Block> blocks = messageService.getMessages();

        return new ResponseEntity<>(blocks, HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.POST, path = {"", "/"})
    public ResponseEntity createMessage(@RequestBody Block msgBlock){
        messageService.addBlock(msgBlock);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{hash}")
    public ResponseEntity<Block> getMessageByHash(@PathVariable int hash){
        Optional<Block> maybeBlock = messageService.getByHash(hash);
        ResponseEntity<Block> response;

        if(maybeBlock.isPresent()){
            response = new ResponseEntity<>(maybeBlock.get(), HttpStatus.ACCEPTED);
        }
        else {
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    @RequestMapping(method = RequestMethod.DELETE, path = {"", "/"})
    public ResponseEntity deleteAll(){
        messageService.deleteAll();

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/valid")
    public ResponseEntity<ValidDto> isChainValid(){
        ValidDto valid = new ValidDto();

        valid.setValid((messageService.isChainValid() ? true : false));

        return new ResponseEntity<>(valid, HttpStatus.ACCEPTED);
    }

}
