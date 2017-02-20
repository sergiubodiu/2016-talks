package io.pivotal.apac;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MeetupController {

    private static final Logger logger = LoggerFactory.getLogger(MeetupController.class);

	@Autowired
	private TalkRepository talkRepository;

	@RequestMapping(value = "/agenda", method = RequestMethod.GET)
	public String index(Model model) throws Exception {
		setModel(model);
		return "index";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String index() throws Exception {
		return "admin";
	}

	private void setModel(Model model) throws Exception {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a");
		String serverTime = dateFormat.format(date);
		model.addAttribute("serverTime", serverTime);

		String port = System.getenv("PORT");
		model.addAttribute("port", port);

		String vcapApplication = System.getenv("VCAP_APPLICATION");
		ObjectMapper mapper = new ObjectMapper();
		if (vcapApplication != null) {
			Map vcapMap = mapper.readValue(vcapApplication, Map.class);
			model.addAttribute("vcapApplication", vcapMap);
		} else {
			model.addAttribute("vcapApplication", new HashMap<>());
		}

		String vcapServices = System.getenv("VCAP_SERVICES");
		model.addAttribute("vcapServices", vcapServices);

		Iterable<Talk> talks = talkRepository.findAll();
		model.addAttribute("talks", talks);
	}

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam("talkId") Long talkId, Model model) throws Exception {
		talkRepository.delete(talkId);
		setModel(model);
		return "redirect:/agenda";
	}

	@RequestMapping(value = "/kill", method = RequestMethod.GET)
	public void kill() {
		
		logger.warn("*** The system is shutting down. ***");
		System.exit(-1);
		
	}

	@RequestMapping(value = "/mem", method = RequestMethod.GET)
	public String memory(@RequestParam(required=true, value="value") Long size) {
		
		size = size * 1024;
		
		//allocate specified memory in jvm
		char[] chars = new char[(size.intValue()/2)]; //divide by 2 since a char is 2 bytes
		Arrays.fill(chars, 'a');
		logger.info("Consumed " + size + "kb");
		return "index";
	}

	@RequestMapping(value = "/cpu", method = RequestMethod.GET)
	public String cpu(@RequestParam(required=true, value="value") Long time) throws Exception{

		StringBuilder sb = new StringBuilder("abcdefghijklmnopqrstuvwxyz");
		
		//allocate consume CPU for specified time
		long start = System.currentTimeMillis();
		while((System.currentTimeMillis() - start) < time) {
			sb.reverse();
			Thread.sleep(0,2);
		}
		logger.info("Consumed CPU for " + time + " millis");
		
		return "index";
	}

}