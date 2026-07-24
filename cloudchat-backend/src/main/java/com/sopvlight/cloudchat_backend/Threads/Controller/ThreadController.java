package com.sopvlight.cloudchat_backend.Threads.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sopvlight.cloudchat_backend.Exception.GeneralException;
import com.sopvlight.cloudchat_backend.Security.Auth.Model.UserData;
import com.sopvlight.cloudchat_backend.Threads.Constants.ThreadConstant;
import com.sopvlight.cloudchat_backend.Threads.DTO.NewChannelThreadRequestDTO;
import com.sopvlight.cloudchat_backend.Threads.DTO.NewGroupThreadRequestDTO;
import com.sopvlight.cloudchat_backend.Threads.DTO.NewPrivateThreadRequestDTO;
import com.sopvlight.cloudchat_backend.Threads.Service.ThreadService;

@RestController
@RequestMapping("/api/threads")
public class ThreadController {
    private ThreadService threadService;
    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }
    @GetMapping("/all/private")
    public ResponseEntity<?> getAllThreadsPrivate(@AuthenticationPrincipal UserData details){
        return new ResponseEntity<>(threadService.getAllThreads(details.getUserId(),ThreadConstant.THREAD_TYPE_PRIVATE), HttpStatus.OK);
    }
    @GetMapping("/all/group")
    public ResponseEntity<?> getAllThreadsGroup(@AuthenticationPrincipal UserData details){
        return new ResponseEntity<>(threadService.getAllThreads(details.getUserId(),ThreadConstant.THREAD_TYPE_GROUP), HttpStatus.OK);
    }
    @GetMapping("/all/channel")
    public ResponseEntity<?> getAllThreadsChannel(@AuthenticationPrincipal UserData details){
        return new ResponseEntity<>(threadService.getAllThreads(details.getUserId(),ThreadConstant.THREAD_TYPE_CHANNEL), HttpStatus.OK);
    }
    @GetMapping("/open/{id}")
    public ResponseEntity<?> getThreadById(@AuthenticationPrincipal UserData details,@PathVariable Long id) throws GeneralException{
        return new ResponseEntity<>(threadService.getThreadById(details.getUserId(), id), HttpStatus.OK);
    }
    @PostMapping("/create/private")
    public ResponseEntity<?> createPrivateThread(@AuthenticationPrincipal UserData details,@RequestBody NewPrivateThreadRequestDTO request) throws GeneralException {
        return new ResponseEntity<>(threadService.createPrivateThread( details.getUserId(), request.username()), HttpStatus.CREATED);
    }
    @PostMapping("/create/group")
    public ResponseEntity<?> createGroupThread(@AuthenticationPrincipal UserData details, @RequestBody NewGroupThreadRequestDTO request) throws GeneralException{
        return new ResponseEntity<>(threadService.createGroupThread(details.getUserId(),request.usernames(),request.name()),HttpStatus.CREATED);
    }
    @PostMapping("/create/channel")
    public ResponseEntity<?> createChannelThread(@AuthenticationPrincipal UserData details, @RequestBody NewChannelThreadRequestDTO request) throws GeneralException{
        return new ResponseEntity<>(threadService.createChannelThread(details.getUserId(),request.name()),HttpStatus.CREATED);
    }
    @PatchMapping("/{threadId}/add/{username}")
    public ResponseEntity<?> addMember(Long threadId,String username,@AuthenticationPrincipal UserData details) throws GeneralException{
        threadService.addMember(threadId,username,details.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
