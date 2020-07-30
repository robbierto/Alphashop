package com.xantrix.webapp.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.xantrix.webapp.domain.Articoli;
import com.xantrix.webapp.domain.FamAssort;
import com.xantrix.webapp.domain.Iva;
import com.xantrix.webapp.exception.NoInfoArtFoundException;
import com.xantrix.webapp.repository.FamAssRepository;
import com.xantrix.webapp.repository.IvaRepository;
import com.xantrix.webapp.service.ArticoliService;

@Controller
@RequestMapping("/articoli")
public class ArticoliController 
{
	@Autowired
	private ArticoliService articoliService;
	
	@Autowired
	private FamAssRepository famAssRepository;
	
	@Autowired
	private IvaRepository ivaRepository;
	
	private int NumArt = 0;
	private List<Articoli> recordset;
	
	private final String PathImages = "static\\images\\Articoli\\";


	//http://localhost:8090/alphashop
	@RequestMapping(method = RequestMethod.GET)
	public String GetArticoli(Model model)
	{
		model.addAttribute("Titolo", "Ricerca Articoli");
		model.addAttribute("Titolo2", "Ricerca gli articoli");
		model.addAttribute("IsArticoli", true);
		
		return "articoli";
	}
	
	@GetMapping(value = "/search")
	public String SearchItem(@RequestParam("filter") String pSearchTerm, Model model)
	{
		recordset = articoliService.SelArticoliByFilter(pSearchTerm);

		if (recordset != null)
			NumArt = recordset.size();

		model.addAttribute("NumArt", NumArt);
		model.addAttribute("Titolo", "Ricerca Articoli");
		model.addAttribute("Titolo2", "Risultati Ricerca " + pSearchTerm);
		model.addAttribute("Articoli", recordset);
		model.addAttribute("IsArticoli", true);
		model.addAttribute("filter", pSearchTerm);

		return "articoli";
	}
	
	// http://localhost:8080/alphashop/articoli/cerca/barilla
	@RequestMapping(value = "/cerca/{filter}", method = RequestMethod.GET)
	public String GetArticoliByFilter(@PathVariable("filter") String Filter, Model model)
	{
		recordset = articoliService.SelArticoliByFilter(Filter);
		
		if (recordset != null)
			NumArt = recordset.size();
		
		articoliService.DelArticolo("test");
		
		model.addAttribute("NumArt", NumArt);
		model.addAttribute("Titolo", "Ricerca Articoli");
		model.addAttribute("Titolo2", "Risultati Ricerca " + Filter);
		model.addAttribute("Articoli", recordset);
		
		return "articoli";

	}
	
	// http://localhost:8080/alphashop/articoli/cerca?filter=barilla&rep=1
	@RequestMapping(value = "/cerca", method = RequestMethod.GET)
	public String GetArticoliByFilter(@RequestParam("filter") String Filter, @RequestParam("rep") int IdRep,
			Model model)
	{

		List<Articoli> recordset = articoliService.SelArticoliByFilter(Filter)
				.stream()
				.filter(u -> u.getIdFamAss() == IdRep).collect(Collectors.toList());

		if (recordset != null)
			NumArt = recordset.size();

		model.addAttribute("NumArt", NumArt);
		model.addAttribute("Titolo", "Ricerca Articoli");
		model.addAttribute("Titolo2", "Risultati Ricerca " + Filter);
		model.addAttribute("Articoli", recordset);

		return "articoli";
	}
	
	// http://localhost:8080/alphashop/articoli/cerca/barilla/parametri;reparti=1,10,15;orderby=codart,desc;paging=0,10
	@RequestMapping(value = "/cerca/{filter}/{parametri}", method = RequestMethod.GET)
	public String GetArticoliByFilterMatrix(@PathVariable("filter") String Filter,
			@MatrixVariable(pathVar = "parametri") Map<String, List<String>> parametri, Model model)
	{
		int NumArt = 0;
		String orderBy = "codart";
		String tipo = "desc";
		Long SkipValue = (long) 0;
		Long LimitValue = (long) 10;

		List<String> IdRep = parametri.get("reparti");
		List<String> OrderBy = parametri.get("orderby");
		List<String> Paging = parametri.get("paging");

		if (OrderBy != null)
		{
			orderBy = OrderBy.get(0);
			tipo = OrderBy.get(1);
		}

		if (Paging != null)
		{
			SkipValue = Long.parseLong(Paging.get(0));
			LimitValue = Long.parseLong(Paging.get(1));
		}

		List<Articoli> recordset = articoliService.SelArticoliByFilter(Filter, orderBy, tipo);

		recordset = recordset
				.stream()
				.filter(u -> IdRep.contains(Integer.toString(u.getIdFamAss())))
				.filter(u -> u.getQtaMag() > 0)
				.filter(u -> u.getPrezzo() > 0)
				.collect(Collectors.toList());

		if (recordset != null)
			NumArt = recordset.size();

		recordset = recordset
				.stream()
				.skip(SkipValue)
				.limit(LimitValue)
				.collect(Collectors.toList());

		/*
		 * if (orderBy.equals("codart") && tipo.equals("asc")) recordset =
		 * recordset.stream().sorted(Comparator.comparing(Articoli::getCodArt))
		 * .collect(Collectors.toList()); else if (orderBy.equals("codart") &&
		 * tipo.equals("desc")) recordset =
		 * recordset.stream().sorted(Comparator.comparing(Articoli::getCodArt).
		 * reversed()) .collect(Collectors.toList());
		 */
		
		 //List<String> Categorie = recordset.stream().map(Articoli::getDesFamAss).distinct().collect(Collectors.toList());
		 

		model.addAttribute("Articoli", recordset);
		model.addAttribute("NumArt", NumArt);
		model.addAttribute("Titolo", "Ricerca Articoli");

		return "articoli";
	}
	
	// http://localhost:8080/AlphaShop/articoli/cerca/barilla/creati?daData=2010-10-31&aData=2015-10-31
	@RequestMapping(value = "/cerca/{filter}/creati", method = RequestMethod.GET)
	public String GetArticoliByFilterDate(@PathVariable("filter") String Filter,
				@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("daData") Date startDate,
				@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("aData") Date endDate, 
				Model model)
	{

			List<Articoli> recordset = articoliService.SelArticoliByFilter(Filter)
					.stream()
					.filter(u -> u.getDataCreaz().after(startDate))
					.filter(U -> U.getDataCreaz().before(endDate))
					.collect(Collectors.toList());

			if (recordset != null)
				NumArt = recordset.size();

			model.addAttribute("NumArt", NumArt);
			model.addAttribute("Titolo", "Ricerca Articoli");
			model.addAttribute("Titolo2", "Risultati Ricerca " + Filter);
			model.addAttribute("Articoli", recordset);

			return "articoli";
	}
	
	// http://localhost:8080/alphashop/articoli/infoart/000087101
	@RequestMapping(value = "/infoart/{codart}", method = RequestMethod.GET)
	public String GetDettArticolo(@PathVariable("codart") String CodArt, Model model, HttpServletRequest request)
	{
			Articoli articolo = null;
			recordset = articoliService.SelArticoliByFilter(CodArt);
			
			boolean IsFileOk = false;
			
			if (recordset == null || recordset.isEmpty())
				throw new NoInfoArtFoundException(CodArt); 
			else
				articolo = recordset.get(0);
			
			try
			{
				String rootDirectory = request.getSession().getServletContext().getRealPath("/");
				String PathName = rootDirectory + PathImages + articolo.getCodArt().trim() + ".png";

				File f = new File(PathName);
				
				IsFileOk = f.exists();
				
			} 
			catch (Exception ex)
			{ 
			}
		
			model.addAttribute("Titolo", "Dettaglio Articolo");
			model.addAttribute("Titolo2", "Dati Articolo " + CodArt);
			model.addAttribute("articolo", articolo);
			model.addAttribute("IsFileOk", IsFileOk);
			
			
			return "infoArticolo";
	} 
	
	@RequestMapping(value = "/cerca/{filter}/download", method = RequestMethod.GET)
	public String GetArticoliByFilterDwld(@PathVariable("filter") String Filter, Model model)
	{
		recordset = articoliService.SelArticoliByFilter(Filter);
		model.addAttribute("Articoli", recordset);

		return "";
	}
	
	@ExceptionHandler(NoInfoArtFoundException.class)
	public ModelAndView handleError(HttpServletRequest request, NoInfoArtFoundException exception)
	{
		ModelAndView mav = new ModelAndView();

		mav.addObject("codice", exception.getCodArt());
		mav.addObject("exception", exception);
		mav.addObject("url", request.getRequestURL() + "?" + request.getQueryString());
		
		mav.setViewName("noInfoArt");

		return mav;
	}
	
	@GetMapping(value = "/aggiungi")
	public String InsArticoli(Model model)
	{
		Articoli articolo = new Articoli();
		
		model.addAttribute("Titolo", "Inserimento Nuovo Articolo");
		model.addAttribute("famAssort", getFamAssort());
		model.addAttribute("iva", getIva());
		model.addAttribute("newArticolo", articolo);
		
		return "insArticolo";
	}
	
	
	
	@GetMapping(value = "/modifica/{CodArt}")
	public String UpdArticoli(Model model, @PathVariable("CodArt") String CodArt)
	{
		Articoli articolo =  articoliService.SelArticoliByFilter(CodArt).get(0);
		
		if (articolo == null)
			throw new NoInfoArtFoundException(CodArt); 
		
		model.addAttribute("Titolo", "Modifica Articolo");
		model.addAttribute("newArticolo", articolo);
		model.addAttribute("famAssort", getFamAssort());
		model.addAttribute("Iva", getIva());

		return "insArticolo";
	}
	
	@ModelAttribute("famAssort")
	public List<FamAssort> getFamAssort()
	{
		List<FamAssort> famAssort = famAssRepository.SelFamAssort();

		return famAssort;
	}

	@ModelAttribute("iva")
	public List<Iva> getIva()
	{
		List<Iva> iva = ivaRepository.SelIva();

		return iva;
	}
	
	@PostMapping(value="/aggiungi")
	public String GestInsArticoli(@Valid @ModelAttribute("newArticolo") Articoli articolo, BindingResult result, 
			HttpServletRequest request)
	{
		MultipartFile productImage = articolo.getImmagine();
		
		if (result.hasErrors())
		{
			return "insArticolo";
		}
		
		if (productImage != null && !productImage.isEmpty())
		{
			try
			{
				String rootDirectory = request.getSession().getServletContext().getRealPath("/");
				String PathName = rootDirectory + PathImages + articolo.getCodArt().trim() + ".png";

				productImage.transferTo(new File(PathName));
				
			} 
			catch (Exception ex)
			{
				throw new RuntimeException("Errore trasferimento file", ex);
			}
		}
		
		if (result.getSuppressedFields().length > 0)
			throw new RuntimeException("ERRORE: Tentativo di eseguire il binding dei seguenti campi NON consentiti: "
					+ StringUtils.arrayToCommaDelimitedString(result.getSuppressedFields()));
		else
		{
			articoliService.InsArticolo(articolo);

		}
		
		return "redirect:/articoli/infoart/" + articolo.getCodArt().trim();
		//return "redirect:/articoli/cerca/" + articolo.getCodArt();
	}
	
	@RequestMapping(value = "/modifica/{CodArt}", method = RequestMethod.POST)
	public String GestUpdArticoli(@Valid @ModelAttribute("newArticolo") Articoli articolo, BindingResult result,
			@PathVariable("CodArt") String CodArt, Model model, HttpServletRequest request)
	{

		if (result.hasErrors())
		{
			return "insArticolo";
		}

		MultipartFile productImage = articolo.getImmagine();

		if (productImage != null && !productImage.isEmpty())
		{
			try
			{
				String rootDirectory = request.getSession().getServletContext().getRealPath("/");
				String PathName = rootDirectory + PathImages + articolo.getCodArt().trim() + ".png";

				productImage.transferTo(new File(PathName));
				
			} 
			catch (Exception ex)
			{
				throw new RuntimeException("Errore trasferimento file", ex);
			}
		}

		if (result.getSuppressedFields().length > 0)
			throw new RuntimeException("ERRORE: Tentativo di eseguire il binding dei seguenti campi NON consentiti: "
					+ StringUtils.arrayToCommaDelimitedString(result.getSuppressedFields()));
		else
		{
			articoliService.InsArticolo(articolo);
		}

		return "redirect:/articoli/infoart/" + CodArt.trim();
	}
	
	@GetMapping(value = "/elimina/{CodArt}")
	public String DelArticolo(@PathVariable("CodArt") String codArt, Model model)
	{
		try
		{
			if (codArt.length() > 0)
			{
				articoliService.DelArticolo(codArt);
			}
		} 
		catch (Exception ex)
		{
			throw new RuntimeException("Errore eliminazione articolo", ex);
		}

		return "redirect:/articoli/";
	}
	
	@InitBinder
	public void initialiseBinder(WebDataBinder binder)
	{
		binder.setAllowedFields("CodArt", "codArt", "descrizione", "um", "pzCart", "pesoNetto", "idIva", "idStatoArt","idFamAss","dataCreaz","language","immagine");

		binder.setDisallowedFields("prezzo");

	}
	
}
