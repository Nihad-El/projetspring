package test.projet.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import test.projet.models.Developpeur;
import test.projet.models.Ticket;
import test.projet.models.User;
import test.projet.repository.TicketRepository;
import test.projet.repository.UserRepository;
import test.projet.services.TicketService;




@Controller
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	
	
	@RequestMapping("/success")
    public void loginPageRedirect(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        
        String role =  authResult.getAuthorities().toString();
        
        if(role.contains("DEV")){
         response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/"));                            
         }
         else if(role.contains("CLIENT")) {
             response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/listeCl"));
         }else if(role.contains("ADMIN")) {
             response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/listeAdmin"));
         }
    }
	

	
	@GetMapping("/")
	public String liste(Model m) {
		
		
		
		
		String username=SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(username);
		User user=userRepository.getUserByUsername(username);
		List<Ticket> tickets=ticketRepository.findByDeveloppeurEmail(user.getDeveloppeur().getEmail());
		System.out.println(user.toString());
		//m.addAttribute("tickets",ticketService.lesTickets());
		m.addAttribute("tickets",tickets);
		return "tickets/liste";
	}

	
	@GetMapping("/listeCl")
	public String listeClient(Model m) {
		String username=SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(username);
		User user=userRepository.getUserByUsername(username);
		List<Ticket> tickets=ticketRepository.findByClientEmail(user.getClient().getEmail());
		System.out.println(user.toString());
		//m.addAttribute("tickets",ticketService.lesTickets());
		m.addAttribute("tickets",tickets);
		return "tickets/liste";
	}
	
	@GetMapping("/listeAdmin")
	public String listeAdmin(Model m) {
		m.addAttribute("tickets",ticketService.lesTicketsN());
		return "tickets/listeadmin";
	}
	
	@GetMapping("/add")
	public String ajout(Model model) {
		Ticket p=new Ticket();
		model.addAttribute("ticket",p);
		return "tickets/ajout";
	}
	

	
	@PostMapping("add")
	public String enregistrer(@ModelAttribute("ticket")Ticket ticket) {
		Ticket p=ticketService.getTicket(ticket.getId_t());
		if(p!=null)
			ticketService.modifier(ticket);
		else
			ticketService.ajouter(ticket);
		return "redirect:/";
	}
	
	@PostMapping("addd")
	public String enregistrerr(@ModelAttribute("ticket")Ticket ticket) {
		Ticket p=ticketService.getTicket(ticket.getId_t());
		if(p!=null)
			ticketService.modifierr(ticket);
		return "redirect:/";
	}

	@GetMapping("/edit/{id}")
	public String modifier(@PathVariable int id, Model model) {
		Ticket p=ticketService.getTicket(id);
		if(p!=null) {
			model.addAttribute("ticket", p);
			return "tickets/ajout";
		}
		return "redirect:/tickets";
	}
	
	@GetMapping("/editt/{id}")
	public String modifierr(@PathVariable int id, Model model) {
		Ticket p=ticketService.getTicket(id);
		if(p!=null) {
			model.addAttribute("ticket", p);
			return "tickets/ajoutt";
		}
		return "redirect:/tickets";
	}
	
	@GetMapping("/delete/{id}")
	public String suppr(@PathVariable int id) {
		ticketService.supprimer(id);
		return "redirect:/";
	}
	

}
