package com.vaibhav.pohastore.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.vaibhav.pohastore.domain.Book;
import com.vaibhav.pohastore.domain.User;
import com.vaibhav.pohastore.domain.UserBilling;
import com.vaibhav.pohastore.domain.UserPayment;
import com.vaibhav.pohastore.domain.UserShipping;
import com.vaibhav.pohastore.domain.security.PasswordResetToken;
import com.vaibhav.pohastore.domain.security.Role;
import com.vaibhav.pohastore.domain.security.UserRole;
import com.vaibhav.pohastore.service.BookService;
import com.vaibhav.pohastore.service.UserPaymentService;
import com.vaibhav.pohastore.service.UserService;
import com.vaibhav.pohastore.service.UserShippingService;
import com.vaibhav.pohastore.serviceimpl.UserSecurityService;
import com.vaibhav.pohastore.utility.INDConstants;
import com.vaibhav.pohastore.utility.MailConstructor;
import com.vaibhav.pohastore.utility.SecurityUtility;

@Controller 
public class NavigationController {
	
   private static Logger LOGG=LoggerFactory.getLogger(NavigationController.class);
	
	@Autowired
	private MailConstructor mailConstructor;
	
	@Autowired
 JavaMailSender  mailSender;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserSecurityService userSecurityService;
	
	@Autowired
	private BookService bookService;

	
	@Autowired
	private UserPaymentService userPaymentService;
	
	
	@Autowired
	private UserShippingService userShippingService;
	
	
	@GetMapping("/")
	public String navigation() {
		return "home";
		
	}
	
	
	
	
	@RequestMapping("/login")
	public  String login(Model model){
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
		
	}
	@RequestMapping("/forgetPassword")
	public  String forgetPassword(Model model,@RequestParam("email")String email,HttpServletRequest request){
		
		model.addAttribute("classActiveForgetPassword", true);
		
		User user=userService.findByEmail(email);
		
		if(user == null) {
			model.addAttribute("emailNotExists",true);
			return "myAccount";
		}
		
String password=SecurityUtility.randomPassword();
		
		String encryptedPassword=SecurityUtility.passwordEncoder().encode(password);
		
		user.setPassword(encryptedPassword);
		
		userService.save(user);
		
		String token=UUID.randomUUID().toString();
		
		//SimpleMailMessage email=new SimpleMailMessage();
		
		userService.createPasswordResetTokenForUser(token, user);
		
		String appUrl="http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		
		
		SimpleMailMessage newEmail=mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);
		
		mailSender.send(newEmail);
		

		model.addAttribute("forgetPasswordEmailSent", true);
		
		return "myAccount";
		
	}
	
	
	@PostMapping("/newUser")
	public String newUserPost(HttpServletRequest request,@ModelAttribute("email") String userEmail,@ModelAttribute("username") String username,Model model) throws Exception {
		model.addAttribute("classActiveNewAccount",true);
		model.addAttribute("email",userEmail);
		model.addAttribute("username",username);
		
		if(userService.findByUsername(username) != null)
		{
			model.addAttribute("usernameExists",true);
			return "myAccount";
		}
		
		
		if(userService.findByEmail(userEmail) !=null) {
			model.addAttribute("emailExists",true);
			return "myAccount";
		}
		
		User user=new User();
		user.setUsername(username);
		user.setEmail(userEmail);
		
		String password=SecurityUtility.randomPassword();
		
		String encryptedPassword=SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		
		Role role=new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		
		Set<UserRole> userRoles=new HashSet<>();
		
		
		userRoles.add( new UserRole(user,role));
		
		userService.createUser(user,userRoles);
		
		String token=UUID.randomUUID().toString();
		
		//SimpleMailMessage email=new SimpleMailMessage();
		
		userService.createPasswordResetTokenForUser(token, user);
		
		String appUrl="http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		
		
		SimpleMailMessage email=mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);
		
		mailSender.send(email);
		
		model.addAttribute("emailSent",true);
		
		
		//LOGG.info(user.toString());

		//System.out.println(user.getId());

		return "myAccount";
	}
	
	
	
	@RequestMapping("/newUser")
	public  String newUser(Model model,@RequestParam("token") String token,Locale locale){
		
		PasswordResetToken passToken=userService.getPasswordResetToken(token);
		LOGG.info("passToken{} ",passToken);
		
		if(passToken==null) {
			String message="Invalid Token";
			LOGG.info("message{} ",message);

			model.addAttribute("message",message);
			return "redirect:badRquest";
		}
		User user=passToken.getUser();
		String username=user.getUsername();
		UserDetails userDetails=userSecurityService.loadUserByUsername(username);
		
		Authentication authentication= new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		model.addAttribute("user",user);

		model.addAttribute("classActiveEdit", true);

		return "myProfile";
		
	}
	
	@RequestMapping("/bookshelf")
	public String bookShelf(Model model) {
		List<Book> bookList=bookService.findAllBook();
		
		model.addAttribute("bookList", bookList);
		
		/*
		 * //Book book=bookList; for (Book book : bookList) {
		 * System.out.println(book.getId()); }
		 */
		return "bookshelf";
		
		
	}
	
	@RequestMapping("/bookDetail")
	public String BookDetails(Model model,Principal principal,@PathParam("id") Long id ) {
		
		 if(principal!=null) {
			 String  username=principal.getName();
			 User user=userService.findByUsername(username);
			 model.addAttribute("user", user);
			 
			 
		 }
		Book book=bookService.findOne(id);
		
		model.addAttribute("book",book);
		
		List<Integer> qtyList=Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);
		
		return "bookDetail";
		
	}
	
	@RequestMapping("/myProfile")
	public String myProfilePage(Model model,Principal principal) {
		
		
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		//model.addAttribute("orderList", user.getOrderList());
		
		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping", userShipping);
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		List<String> stateList = INDConstants.listOfIndianStateCodes;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("classActiveEdit", true);
		
		return "myProfile";
	}
	
	@RequestMapping("/listOfCreditCards")
	public String listOfCreditCards(
			Model model, Principal principal, HttpServletRequest request
			) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		//model.addAttribute("orderList", user.getOrderList());
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		return "myProfile";
	}
	
	@RequestMapping("/listOfShippingAddresses")
	public String listOfShippingAddresses(
			Model model, Principal principal, HttpServletRequest request
			) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
	//	model.addAttribute("orderList", user.getOrderList());
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		
		return "myProfile";
	}
	
	@RequestMapping("/addNewCreditCard")
	public String addNewCreditCard(
			Model model, Principal principal
			){
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		
		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		UserBilling userBilling = new UserBilling();
		UserPayment userPayment = new UserPayment();
		
		
		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);
		
		List<String> stateList = INDConstants.listOfIndianStateCodes;

		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		//model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	@RequestMapping(value="/addNewShippingAddress", method=RequestMethod.POST)
	public String addNewShippingAddressPost(
			@ModelAttribute("userShipping") UserShipping userShipping,
			Principal principal, Model model
			){
		User user = userService.findByUsername(principal.getName());
		userService.updateUserShipping(userShipping, user);
		
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		//model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	
	@RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(
			Model model, Principal principal
			){
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		
		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		
		UserShipping userShipping = new UserShipping();
		
		model.addAttribute("userShipping", userShipping);
		
		List<String> stateList = INDConstants.listOfIndianStateCodes;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		//model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	
	@PostMapping("addNewCreditCard")
	public String addNewCreditCard(
			@ModelAttribute("userPayment") UserPayment userPayment,
			@ModelAttribute("userBilling") UserBilling userBilling,
			Principal principal, Model model
			){
		User user = userService.findByUsername(principal.getName());
		userService.updateUserBilling(userBilling, userPayment, user);
		
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
	//	model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	@RequestMapping("/updateCreditCard")
	public String updateCreditCard(
			@ModelAttribute("id") Long creditCardId, Principal principal, Model model
			) {
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(creditCardId);
		
		if(user.getId() != userPayment.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
	    	UserBilling userBilling = userPayment.getUserBilling();
			model.addAttribute("userPayment", userPayment);
			model.addAttribute("userBilling", userBilling);
			
			List<String> stateList = INDConstants.listOfIndianStateCodes;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			model.addAttribute("addNewCreditCard", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddresses", true);
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
		//	model.addAttribute("orderList", user.getOrderList());
			
			return "myProfile";
		}
		
		
		
	}
	
	@RequestMapping("/removeCreditCard")
	public String removeCreditCard(
			@ModelAttribute("id") Long creditCardId, Principal principal, Model model
			){
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(creditCardId);
		
		if(user.getId() != userPayment.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			userPaymentService.removeById(creditCardId);
			
			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddresses", true);
			
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			//model.addAttribute("orderList", user.getOrderList());
			
			return "myProfile";
		}
	}
	
	@RequestMapping(value="/setDefaultPayment", method=RequestMethod.POST)
	public String setDefaultPayment(
			@ModelAttribute("defaultUserPaymentId") Long defaultPaymentId, Principal principal, Model model
			) {
		User user = userService.findByUsername(principal.getName());
		System.out.println("****************************************************"+defaultPaymentId);
		userService.setUserDefaultPayment(defaultPaymentId, user);
		
		model.addAttribute("user", user);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
	//	model.addAttribute("orderList", user.getOrderList());
		return "myProfile";
	}		
	
	@RequestMapping("/updateUserShipping")
	public String updateUserShipping(
			@ModelAttribute("id") Long shippingAddressId, Principal principal, Model model
			) {
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(shippingAddressId);
		
		if(user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			
			model.addAttribute("userShipping", userShipping);
			
			List<String> stateList = INDConstants.listOfIndianStateCodes;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			model.addAttribute("addNewShippingAddress", true);
			model.addAttribute("classActiveShipping", true);
			model.addAttribute("listOfCreditCards", true);
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
		//	model.addAttribute("orderList", user.getOrderList());
			
			return "myProfile";
		}
	}
	@RequestMapping(value="/setDefaultShippingAddress", method=RequestMethod.POST)
	public String setDefaultShippingAddress(
			@ModelAttribute("defaultShippingAddressId") Long defaultShippingId, Principal principal, Model model
			) {
		User user = userService.findByUsername(principal.getName());
		userService.setUserDefaultShipping(defaultShippingId, user);
		
		model.addAttribute("user", user);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
	//	model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	
	@RequestMapping("/removeUserShipping")
	public String removeUserShipping(
			@ModelAttribute("id") Long userShippingId, Principal principal, Model model
			){
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);
		
		if(user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			
			userShippingService.removeById(userShippingId);
			
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveShipping", true);
			
			System.out.println("#############################################"+user.getUserShippingList());
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
		//  model.addAttribute("orderList", user.getOrderList());
			
			return "myProfile";
		}
	}
}
