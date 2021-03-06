package co.messagesblockchain.app.controllers;

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

    @RequestMapping(method = RequestMethod.GET, path = "/getrange")
    public ResponseEntity<List<Block>> getByRange(){
        List<Block> rangeOfBlocks = messageService.getRangeOfBlocks();

        return new ResponseEntity<>(rangeOfBlocks, HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{hash}")
    public ResponseEntity<Block> getMessageByHash(@PathVariable int hash){
        Optional<Block> maybeBlock = messageService.getByHash(hash);
        ResponseEntity<Block> response;

        response = maybeBlock.map(block -> new ResponseEntity<>(block, HttpStatus.ACCEPTED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        return response;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/block/{number}")
    public ResponseEntity<Block> retrieveByBlockNumber(@PathVariable Integer number){
        ResponseEntity<Block> response;

        Optional<Block> maybeBlock = messageService.getByNumber(number);

        response = maybeBlock.map(block -> new ResponseEntity<>(block, HttpStatus.ACCEPTED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

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

        valid.setValid(messageService.isChainValid());

        return new ResponseEntity<>(valid, HttpStatus.ACCEPTED);
    }

}
