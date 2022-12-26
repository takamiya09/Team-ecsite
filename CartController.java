package jp.co.internous.utopia.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.utopia.model.domain.TblCart;
import jp.co.internous.utopia.model.domain.dto.CartDto;
import jp.co.internous.utopia.model.form.CartForm;
import jp.co.internous.utopia.model.mapper.TblCartMapper;
import jp.co.internous.utopia.model.session.LoginSession;

@Controller
@RequestMapping("/utopia/cart")
public class CartController {
	@Autowired
	LoginSession loginSession;

	@Autowired
	TblCartMapper tblCartMapper;

	private Gson gson = new Gson();

	@RequestMapping("/")
	public String index(Model model) {
		int userId = loginSession.isLoginflg() ? loginSession.getId() : loginSession.getGuestId();
		List<CartDto> cart = tblCartMapper.findByUserId(userId);

		model.addAttribute("loginSession", loginSession);
		model.addAttribute("cart", cart);

		return "cart";
	}

	@RequestMapping("/add")
	public String addCart(CartForm cartForm, Model model) {
		int userId = loginSession.isLoginflg() ? loginSession.getId() : loginSession.getGuestId();
		cartForm.setUserId(userId);

		TblCart tblCart = new TblCart(cartForm);
		int result = 0;
		if (tblCartMapper.findCountByUserIdAndProductId(userId, cartForm.getProductId()) > 0) {
			result = tblCartMapper.update(tblCart);
		} else {
			result = tblCartMapper.insert(tblCart);
		}

		if (result > 0) {
			List<CartDto> cart = tblCartMapper.findByUserId(userId);
			model.addAttribute("loginSession", loginSession);
			model.addAttribute("cart", cart);
		}

		return "cart";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/delete")
	@ResponseBody
	public boolean removeCart(@RequestBody String checkedIdList) {
		int result = 0;

		Map<String, List<Integer>> map = gson.fromJson(checkedIdList, Map.class);
		List<Integer> checkedIds = map.get("checkedIdList");

		result = tblCartMapper.deleteById(checkedIds);

		return result > 0;
	}
}
