package jp.co.internous.utopia.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import jp.co.internous.utopia.model.domain.TblCart;
import jp.co.internous.utopia.model.domain.dto.CartDto;

@Mapper
public interface TblCartMapper {
	List<CartDto> findByUserId(int userId);

	@Select("SELECT count(id) FROM tbl_cart WHERE user_id=#{userId} AND product_id=#{productId}")
	int findCountByUserIdAndProductId(@Param("userId") int userId, @Param("productId") int productId);

	@Update("UPDATE tbl_cart SET product_count = product_count+#{productCount},"
			+ "updated_at=now() WHERE user_id=#{userId} AND product_id=#{productId}")
	int update(TblCart tblCart);

	@Insert("INSERT INTO tbl_cart (user_id, product_id, product_count)"
			+ "VALUES(#{userId},#{productId}, #{productCount})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insert(TblCart tblCart);

	@Select("SELECT count(user_id) FROM tbl_cart WHERE user_id= #{userId}")
	int findCountByUserId(int userId);

	@Update("UPDATE tbl_cart SET user_id=#{id}, updated_at=now() WHERE user_id=#{guestId}")
	int updateUserId(@Param("id") int id, @Param("guestId") int guestId);

	int deleteById(@Param("checkedIds") List<Integer> checkedIds);

	@Delete("DELETE FROM tbl_cart WHERE user_id = #{userId}")
	int deleteByUserId(int userId);

}
