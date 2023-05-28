package com.board.security.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.security.domain.Board;
import com.board.security.domain.Reply;
import com.board.security.domain.ResponseDTO;
import com.board.security.service.BoardService;
import com.board.security.utils.MediaUtils;
import com.board.security.utils.Pager;

@Controller
public class BoardController {

	private final static Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired
	BoardService boardService;
	
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;
	
	@GetMapping("/")
	public String list(Model model, @RequestParam(defaultValue = "1") int curPage){
		int count = boardService.count();
		Pager pager = new Pager(count, curPage);
		int start = pager.getPageStart() - 1;
		int end = Pager.getBlockScale();
		model.addAttribute("dto", boardService.list(start, end));
		model.addAttribute("page", pager);
		return "index";
	}
	
	@GetMapping("/detailForm/{num}")
	public String detailForm(@PathVariable int num, Model model) {
		model.addAttribute("dto", boardService.detail(num));
		return "board/detailForm";
	}
	
	@GetMapping("/board/writeForm")
	public String writeForm() {
		return "board/writeForm";
	}
	
	@GetMapping("/commentList/{bnum}")
	public String commentList(@PathVariable int bnum, String username, Model model) {
		model.addAttribute("reply", boardService.listComment(bnum));
		model.addAttribute("username", username);
		return "board/commentList";
	}
	
	@ResponseBody
	@PostMapping("/board/comment")
	public ResponseDTO<String> comment(@RequestBody Reply reply) {
		boardService.insertComment(reply);
		return new ResponseDTO<String>(HttpStatus.OK.value(), "Insert Comment Success!");
	}

	@ResponseBody
	@DeleteMapping("/deleteComment/{rnum}")
	public ResponseDTO<String> deleteComment(@PathVariable int rnum) {
		boardService.deleteComment(rnum);
		return new ResponseDTO<String>(HttpStatus.OK.value(), "Delete Comment Success!");
	}
	
	@GetMapping("/board/{num}/updateForm")
	public String updateForm(@PathVariable int num, Model model) {
		model.addAttribute("dto", boardService.detail(num));
		return "board/updateForm";
	}
	
	@PostMapping("/board/update")
	public String update(@ModelAttribute Board board) {
		boardService.update(board);
		return "redirect:/";
	}
	
	@ResponseBody
	@GetMapping("/auth/preview/{num}")
	public String preview(@PathVariable int num) {
		return boardService.preview(num);
	}
	
	@GetMapping("/board/{num}/replyForm")
	public String replyForm(@PathVariable int num, Model model) {
		Board dto = boardService.detail(num);
		dto.setSubject("Re : "+dto.getSubject());
		dto.setContent("=============== Original Content ===============<br/>"+dto.getContent());
		model.addAttribute("dto", dto);
		return "board/replyForm";
	}
	
	@ResponseBody
	@PostMapping("/board/reply")
	public ResponseDTO<String> reply(@RequestBody Board board) {
		boardService.insertReply(board);
		return new ResponseDTO<String>(HttpStatus.OK.value(), "Insert Reply Success!");
	}
	
	@PostMapping("/board/insert")
	public String insert(@ModelAttribute Board board) {
		boardService.insertBoard(board);
		return "redirect:/";
	}
	
	@ResponseBody
	@GetMapping("/board/deleteFile")
	public ResponseEntity<String> deleteFile(String filename) {
		String fileExtention=filename.substring(filename.lastIndexOf(".")+1);
		MediaType mediaType=MediaUtils.getMediaType(fileExtention);
		if(mediaType != null) {
			String start=filename.substring(0, 12);
			String end=filename.substring(14);
			new File(uploadPath+(start+end).replace('/', File.separatorChar)).delete();
		}
		new File(uploadPath+filename.replace('/', File.separatorChar)).delete();
		boardService.deleteAttach(filename);
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping("/board/downloadFile")
	public ResponseEntity<byte[]> downloadFile(String filename) throws Exception {
		InputStream inputStream=null;
		HttpHeaders httpHeaders=new HttpHeaders();
		try {
			inputStream=new FileInputStream(uploadPath+filename);
			filename=filename.substring(filename.lastIndexOf("_")+1);
			filename=new String(filename.getBytes("utf-8"), "8859_1");
			httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			httpHeaders.add("Content-Disposition", "attachment;filename=\""+filename+"\"");
			return new ResponseEntity<byte[]>(IOUtils.toByteArray(inputStream), httpHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			if(inputStream != null) inputStream.close();
		}
	}
	
	@GetMapping("/board/attachList/{bnum}")
	public @ResponseBody List<String> attachList(@PathVariable int bnum) {
		return boardService.listAttach(bnum);
	}
	
	@GetMapping("/board/delete")
	public String delete(@ModelAttribute Board dto) {
		boardService.delete(dto);
		return "redirect:/";
	}
	
}
