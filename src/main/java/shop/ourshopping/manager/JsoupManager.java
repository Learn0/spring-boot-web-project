package shop.ourshopping.manager;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import shop.ourshopping.component.OSPath;
import shop.ourshopping.dto.mybatis.MusicDTO;
import shop.ourshopping.dto.mybatis.SeoulAttractionsDTO;
import shop.ourshopping.service.MovieService;
import shop.ourshopping.service.MusicService;
import shop.ourshopping.service.RecipeService;
import shop.ourshopping.service.RestaurantService;
import shop.ourshopping.service.SeoulAttractionsService;
import shop.ourshopping.utils.YoutubeUtils;
import shop.ourshopping.vo.MovieVO;
import shop.ourshopping.vo.RecipeVO;
import shop.ourshopping.vo.RestaurantVO;

// HTML 파싱
@Component
@AllArgsConstructor
public class JsoupManager {

	private OSPath osPath;
	private JsonManager jsonManager;
	@Qualifier("movieServiceImpl")
	private MovieService movieService;
	@Qualifier("musicServiceImpl")
	private MusicService musicService;
	@Qualifier("recipeServiceImpl")
	private RecipeService recipeService;
	@Qualifier("restaurantServiceImpl")
	private RestaurantService restaurantService;
	@Qualifier("seoulAttractionsServiceImpl")
	private SeoulAttractionsService seoulAttractionsService;

	public String readBoxOffice(int type) {
		String json = "";
		try {
			String url;
			switch (type) {
			case 1:// 일일 박스오피스
				url = "https://www.kobis.or.kr/kobis/business/main/searchMainDailyBoxOffice.do";
				break;
			case 2:// 실시간 예매율
				url = "https://www.kobis.or.kr/kobis/business/main/searchMainRealTicket.do";
				break;
			case 3:// 독립 예술
				url = "https://www.kobis.or.kr/kobis/business/main/searchMainDiverMov.do";
				break;
			case 4:// 독릭 예술 예매율
				url = "https://www.kobis.or.kr/kobis/business/main/searchMainDiverRealTicket.do";
				break;
			case 5:// 좌석 점유율
				url = "https://www.kobis.or.kr/kobis/business/main/searchMainDailySeatTicket.do";
				break;
			default:// 온라인 상영관
				url = "https://www.kobis.or.kr/kobis/business/main/searchMainOnlineDailyBoxOffice.do";
				break;
			}
			Document doc = Jsoup.connect(url).get();
			Element body = doc.selectFirst("body");

			json = jsonManager.boxOfficeJson(body.text());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	public String searchMovieLink(String keyword) {
		String site = "https://movie.naver.com/movie";
		try {
			String url = "https://movie.naver.com/movie/search/result.naver?query="
					+ URLEncoder.encode(keyword, "UTF-8");
			Document doc = Jsoup.connect(url).get();
			Element link = doc.selectFirst(".result_thumb>a");
			if (link != null) {
				site += link.attr("href");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return site;
	}

	public boolean downloadMusic() {
		try {
			musicService.deleteMusic();

			int idx = 1;
			String url = "https://genie.co.kr/chart/top200?ditc=D&pg=";
			for (int i = 1; i < 5; i++) {
				Document doc = Jsoup.connect(url + i).get();
				Elements poster = doc.select("td a.cover img");
				Elements title = doc.select("td a.title.ellipsis");
				Elements singer = doc.select("td a.artist.ellipsis");
				Elements album = doc.select("td a.albumtitle.ellipsis");
				Elements etc = doc.select("td span span.rank");
				for (int j = 0; j < title.size(); j++) {
					try {
						String state = etc.get(j).text().replace(" ", "");
						String incrment = "";
						if (state.equals("유지")) {
							incrment = "0";
						} else if (state.equals("new")) {
							incrment = String.valueOf(200 - idx);
						} else {
							incrment = state.replaceAll("[^0-9]", "");
							state = state.replaceAll("[^가-힣]", "");
						}

						MusicDTO vo = new MusicDTO();
						vo.setIdx(idx);
						vo.setTitle(title.get(j).text());
						vo.setSinger(singer.get(j).text());
						vo.setAlbum(album.get(j).text());
						Path uploadPath = osPath.getPath("music");
						System.out.println(vo.getTitle() + poster.get(j).attr("src"));
						String fileURL = imageURL(uploadPath, poster.get(j).attr("src"), String.valueOf(idx));
						if (fileURL == null) {
							throw new RuntimeException();
						}
						vo.setPoster(fileURL);
						vo.setState(state);
						vo.setIncrement(Integer.valueOf(incrment));
						vo.setYoutubeKey(YoutubeUtils.getKey(vo.getTitle()));

						musicService.insertMusic(vo);
						Thread.sleep(500);
						idx += 1;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}

	public boolean downloadRestaurant(String search) {
		try {
			int page = 1;
			String url = "https://www.mangoplate.com/search?keyword=" + URLEncoder.encode(search, "UTF-8") + "&page=";
			while (true) {
				Document doc = Jsoup.connect(url + page).get();
				Elements title = doc.select("div.info h2.title");
				if (title.size() == 0) {
					break;
				} else {
					Elements content = doc.select("li.server_render_search_result_item div.info p.etc");
					Elements score = doc.select("li.server_render_search_result_item strong.point.search_point");
					Elements info = doc.select("div.info a");
					for (int i = 0; i < title.size(); i++) {
						try {
							String infoUrl = url.substring(0, url.indexOf("/search"));
							infoUrl += info.get(i).attr("href");
							Document infoDoc = Jsoup.connect(infoUrl).get();
							Elements poster = infoDoc.select("figure.restaurant-photos-item img.center-croping");
							Element hashtag = infoDoc.selectFirst("ul.Restaurant__TagList.only-mobile");
							Elements address = infoDoc.select("div.Restaurant__InfoItemLabel div");
							Element phoneNumber = infoDoc.selectFirst("a div.Restaurant__InfoItemLabel");
							Elements etc = infoDoc.select("table.info tr");
							Element review = infoDoc.selectFirst("script#reviewCountInfo");

							RestaurantVO vo = new RestaurantVO();
							vo.setTitle(title.get(i).text());
							vo.setContent(content.get(i).text());
							if (score.get(i).text().isEmpty()) {
								vo.setScore(BigDecimal.valueOf(0));
							} else {
								vo.setScore(new BigDecimal(score.get(i).text()));
							}
							vo.setViewCount(0);
							Path uploadPath = osPath.getPath("restaurant");
							if (!Files.isDirectory(uploadPath)) {
								Files.createDirectory(uploadPath);
								if (!osPath.getOS().contains("win")) {
									try {
										Runtime.getRuntime().exec("chmod -R 755 " + uploadPath);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							StringBuffer sb = new StringBuffer();
							String match = "[^\uAC00-\uD7A30-9a-zA-Z]";
							for (int j = 0; j < poster.size(); j++) {
								System.out.println(vo.getTitle() + poster.get(j).attr("src"));
								String fileURL = imageURL(
										Paths.get(uploadPath.toString(),
												vo.getTitle()
														.substring(0,
																vo.getTitle().length() > 10 ? 10
																		: vo.getTitle().length())
														.replaceAll(match, "-")
														+ "("
														+ vo.getContent()
																.substring(0,
																		vo.getContent().length() > 10 ? 10
																				: vo.getContent().length())
																.replaceAll(match, "-")
														+ ")"),
										poster.get(j).attr("src"), String.valueOf(j));
								if (fileURL == null) {
									continue;
								}
								sb.append(fileURL);
								sb.append("^");
							}
							String image = sb.toString();
							if (!image.isEmpty()) {
								image = image.substring(0, image.lastIndexOf("^"));
							} else {
								throw new RuntimeException();
							}
							vo.setPoster(image);
							vo.setHashtag(hashtag.text());
							if (address.size() > 0) {
								vo.setAddress(address.get(0).text());
								if (address.size() > 1) {
									vo.setOldAddress(address.get(1).text());
								}
							} else {
								continue;
							}
							if (!phoneNumber.text().isEmpty()) {
								vo.setPhoneNumber(phoneNumber.text());
							}
							for (int j = 0; j < etc.size(); j++) {
								String type = etc.get(j).selectFirst("th").text();
								if (type.equals("음식 종류")) {
									vo.setFoodType(etc.get(j).selectFirst("td").text());
								} else if (type.equals("가격대")) {
									vo.setPrice(etc.get(j).selectFirst("td").text());
								} else if (type.equals("주차")) {
									vo.setParking(etc.get(j).selectFirst("td").text());
								} else if (type.equals("영업시간")) {
									vo.setBusinessTime(etc.get(j).selectFirst("td").text());
								} else if (type.equals("메뉴")) {
									vo.setMenu(etc.get(j).selectFirst("td").text());
								} else if (type.equals("웹 사이트")) {
									vo.setSite(etc.get(j).selectFirst("td a").attr("href"));
								}
							}
							int[] point = jsonManager.reviewJson(review.data());
							vo.setBad(point[0]);
							vo.setSoso(point[1]);
							vo.setGood(point[2]);

							restaurantService.insertRestaurant(vo);
							Thread.sleep(500);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				page += 1;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}

	public boolean downloadRecipe() {
		try {
			int page = 1;
			String url = "https://www.10000recipe.com/recipe/list.html?order=reco&page=";
			while (true) {
				Document doc = Jsoup.connect(url + page).get();
				Elements title = doc.select(".common_sp_caption > .common_sp_caption_tit");
				if (title.size() == 0) {
					break;
				} else {
					Elements writer = doc.select(".common_sp_caption_rv_name");
					Elements info = doc.select(".common_sp_thumb a.common_sp_link");
					for (int i = 0; i < title.size(); i++) {
						try {
							String infoUrl = url.substring(0, url.indexOf("/recipe"));
							infoUrl += info.get(i).attr("href");
							Document infoDoc = Jsoup.connect(infoUrl).get();
							Elements poster = infoDoc.select(".view_step_cont >  div > img");
							Element hashtag = infoDoc.selectFirst(".view_tag");
							Element content = infoDoc.selectFirst(".view2_summary.st3 > div");
							Element amount = infoDoc.selectFirst(".view2_summary_info1");
							Element time = infoDoc.selectFirst(".view2_summary_info2");
							Element difficulty = infoDoc.selectFirst(".view2_summary_info3");
							Elements cookingOrder = infoDoc.select(".view_step div.media-body");
							Element tip = infoDoc.selectFirst(".view_step_tip");

							RecipeVO vo = new RecipeVO();
							vo.setTitle(title.get(i).text());
							vo.setWriter(writer.get(i).text());
							vo.setViewCount(0);
							Path uploadPath = osPath.getPath("recipe");
							if (!Files.isDirectory(uploadPath)) {
								Files.createDirectory(uploadPath);
								if (!osPath.getOS().contains("win")) {
									try {
										Runtime.getRuntime().exec("chmod -R 755 " + uploadPath);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							StringBuffer sb = new StringBuffer();
							String match = "[^\uAC00-\uD7A30-9a-zA-Z]";
							for (int j = 0; j < poster.size(); j++) {
								System.out.println(vo.getTitle() + poster.get(j).attr("src"));
								String fileURL = imageURL(
										Paths.get(uploadPath.toString(),
												vo.getTitle()
														.substring(0,
																vo.getTitle().length() > 10 ? 10
																		: vo.getTitle().length())
														.replaceAll(match, "-")
														+ "("
														+ vo.getWriter()
																.substring(0,
																		vo.getWriter().length() > 10 ? 10
																				: vo.getWriter().length())
																.replaceAll(match, "-")
														+ ")"),
										poster.get(j).attr("src"), String.valueOf(j));
								if (fileURL == null) {
									throw new RuntimeException();
								}
								sb.append(fileURL);
								sb.append("^");
							}
							String image = sb.toString();
							if (!image.isEmpty()) {
								image = image.substring(0, image.lastIndexOf("^"));
							}
							vo.setPoster(image);
							if (hashtag != null) {
								vo.setHashtag(hashtag.text());
							}
							if (content != null) {
								vo.setContent(content.text());
							}
							if (amount != null) {
								vo.setAmount(amount.text());
							}
							if (time != null) {
								vo.setTime(time.text());
							}
							if (difficulty != null) {
								vo.setDifficulty(difficulty.text());
							}
							sb.setLength(0);
							for (int j = 0; j < cookingOrder.size(); j++) {
								sb.append(cookingOrder.get(j).text());
								sb.append("^");
							}
							String cooking = sb.toString();
							if (!cooking.isEmpty()) {
								cooking = cooking.substring(0, cooking.lastIndexOf("^"));
							}
							vo.setCookingOrder(cooking);
							if (tip != null) {
								vo.setTip(tip.text());
							}

							recipeService.insertRecipe(vo);
							Thread.sleep(500);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				page += 1;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}

	public boolean downloadMovie() {
		try {
			movieService.deleteMovie();

			int idx = 1;
			String url = "https://movie.naver.com/movie/running/current.naver";
			Document doc = Jsoup.connect(url).get();
			Elements title_ = doc.select("dt.tit");
			Elements score = doc.select(".st_off+span.num");
			Elements reserve = doc.select(".info_exp div.star_t1");
			Elements etc_ = doc.select("dl.info_txt1");
			Elements info = doc.select("div.thumb a");
			for (int i = 0; i < title_.size(); i++) {
				try {
					Element title = title_.get(i).selectFirst("a");
					Element grade = title_.get(i).selectFirst("span");
					Element etc = etc_.get(i).selectFirst("dt.tit_t1+dd");
					Element director = etc_.get(i).selectFirst("dt.tit_t2+dd");
					Element actor = etc_.get(i).selectFirst("dt.tit_t3+dd");

					String infoUrl = url.substring(0, url.lastIndexOf("/movie"));
					infoUrl += info.get(i).attr("href");
					System.out.println(infoUrl);
					Document infoDoc = Jsoup.connect(infoUrl).get();
					Element poster = infoDoc.selectFirst(".poster:nth-child(2) img");
					Element story = infoDoc.selectFirst("p.con_tx");
					Element showUser = infoDoc.selectFirst("span.count");

					StringTokenizer st = new StringTokenizer(etc.text(), "|");
					String genre = st.nextToken().trim();
					String time = "-";
					String regdate = "-";
					while (st.hasMoreTokens()) {
						String token = st.nextToken().trim();
						if (token.contains("분")) {
							time = token;
						} else if (token.contains("개봉")) {
							regdate = token;
						}
					}

					MovieVO vo = new MovieVO();
					vo.setIdx(idx);
					vo.setTitle(title.text());
					if (score.text().isEmpty()) {
						vo.setScore(BigDecimal.valueOf(0));
					} else {
						vo.setScore(new BigDecimal(score.get(i).text().replaceAll("[가-힣]", "").trim()));
					}
					if (grade != null) {
						vo.setGrade(grade.text());
					} else {
						vo.setGrade("전체 관람가");
					}
					if (reserve != null && reserve.size() > i) {
						vo.setReserve(reserve.get(i).text());
					} else {
						vo.setReserve("0%");
					}
					vo.setGenre(genre);
					vo.setTime(time);
					vo.setRegdate(regdate);
					vo.setDirector(director.text());
					if (actor != null) {
						vo.setActor(actor.text());
					} else {
						vo.setActor("-");
					}
					if (showUser != null) {
						vo.setShowUser(showUser.text());
					} else {
						vo.setShowUser("0명");
					}
					vo.setViewCount(0);
					Path uploadPath = osPath.getPath("movie");
					System.out.println(vo.getTitle() + poster.attr("src"));
					String fileURL = imageURL(uploadPath, poster.attr("src"), String.valueOf(i));
					if (fileURL == null) {
						throw new RuntimeException();
					}
					vo.setPoster(fileURL);
					if (story != null) {
						vo.setStory(story.text());
					} else {
						vo.setStory("-");
					}
					vo.setYoutubeKey(YoutubeUtils.getKey(vo.getTitle()));

					movieService.insertMovie(vo);
					Thread.sleep(500);
					idx += 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}

	public boolean downloadAttractions() {
		String[] attractionsArr = { "attractions", "entertainment", "hotels", "nature", "restaurants", "seoul-stay",
				"shopping" };
		for (String attractions : attractionsArr) {
			try {
				int page = 1;
				String url = "https://korean.visitseoul.net/" + attractions + "?curPage=";
				while (true) {
					Document doc = Jsoup.connect(url + page).get();
					Elements title = doc.select("ul.article-list li.item div.infor-element span.title");
					if (title.size() == 0) {
						break;
					} else {
						Elements content = doc.select("ul.article-list li.item div.infor-element span.text-dot-d");
						Elements score = doc.select("div.infor-element-inner span.trip-ico img");
						Elements info = doc.select("ul.article-list li.item a");
						for (int i = 0; i < title.size(); i++) {
							try {
								String infoUrl = url.substring(0, url.indexOf("/" + attractions));
								infoUrl += info.get(i).attr("href");
								Document infoDoc = Jsoup.connect(infoUrl).get();
								Elements poster = infoDoc.select("div.wide-slide div.item");
								Elements etc = infoDoc.select(".detail-map-infor dl");

								SeoulAttractionsDTO vo = new SeoulAttractionsDTO();
								vo.setType(attractions);
								vo.setTitle(title.get(i).text());
								if (content != null && content.size() > i) {
									vo.setContent(content.get(i).text());
								} else {
									vo.setContent("-");
								}
								if (score != null && score.size() > i) {
									String score_ = score.get(i).attr("alt");
									vo.setScore(new BigDecimal(score_.substring(score_.indexOf(":") + 1)));
								} else {
									vo.setScore(BigDecimal.valueOf(0));
								}
								vo.setViewCount(0);
								Path uploadPath = osPath.getPath("seoulAttractions");
								if (!Files.isDirectory(uploadPath)) {
									Files.createDirectory(uploadPath);
									if (!osPath.getOS().contains("win")) {
										try {
											Runtime.getRuntime().exec("chmod -R 755 " + uploadPath);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
								StringBuffer sb = new StringBuffer();
								String match = "[^\uAC00-\uD7A30-9a-zA-Z]";
								for (int j = 0; j < poster.size(); j++) {
									String image_ = poster.get(j).attr("style");
									System.out.println(image_);
									if (image_.contains("('")) {
										image_ = image_.substring(image_.indexOf("('") + 2, image_.lastIndexOf("')"));
									} else {
										image_ = image_.substring(image_.indexOf("(") + 1, image_.lastIndexOf(")"));
									}
									if (image_.indexOf("/") == 0) {
										image_ = url.substring(0, url.indexOf("/" + attractions)) + image_;
									}
									System.out.println(vo.getTitle() + image_);
									String fileURL = imageURL(
											Paths.get(uploadPath.toString(), vo.getTitle()
													.substring(0,
															vo.getTitle().length() > 10 ? 10 : vo.getTitle().length())
													.replaceAll(match, "-")
													+ "("
													+ vo.getContent()
															.substring(0,
																	vo.getContent().length() > 10 ? 10
																			: vo.getContent().length())
															.replaceAll(match, "-")
													+ ")"),
											image_, String.valueOf(j));
									if (fileURL == null) {
										continue;
									}
									sb.append(fileURL);
									sb.append("^");
								}
								String image = sb.toString();
								if (!image.isEmpty()) {
									image = image.substring(0, image.lastIndexOf("^"));
								} else {
									throw new RuntimeException();
								}
								vo.setPoster(image);
								for (int j = 0; j < etc.size(); j++) {
									String type = etc.get(j).selectFirst("dt").text();
									if (type.equals("주소")) {
										vo.setAddress(etc.get(j).selectFirst("dd").text());
									} else if (type.equals("이용시간")) {
										vo.setBusinessTime(etc.get(j).selectFirst("dd").text());
									} else if (type.equals("운영 요일")) {
										vo.setBusinessWeek(etc.get(j).selectFirst("dd").text());
									} else if (type.equals("웹사이트") || type.equals("사이트")) {
										if (etc.get(j).selectFirst("a") != null) {
											vo.setSite(etc.get(j).selectFirst("a").attr("href"));
										}
									} else if (type.equals("이것만은 꼭!")) {
										vo.setTip(etc.get(j).selectFirst("dd").text());
									}
								}

								seoulAttractionsService.insertAttractions(vo);
								Thread.sleep(500);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					page += 1;
				}
			} catch (Exception e) {
				e.printStackTrace();

				return false;
			}
		}
		return true;
	}

	private String imageURL(Path uploadPath, String srcUrl, String fileName) {
		if (!Files.isDirectory(uploadPath)) {
			try {
				Files.createDirectory(uploadPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		srcUrl = srcUrl.replace(":\\", "://").replace("\\", "/");
		String filePath = Paths.get(uploadPath.toString(), fileName + ".jpg").toString();
		String saveFilePath = null;
		for (int i = 0; i < 2; i++) {
			String http = "";
			try {
				if (srcUrl.indexOf("//") == 0) {
					if (i == 0)
						http = "https:";
					else
						http = "http:";
				}
				System.out.println(http + srcUrl);
				URL imgUrl = new URL(http + srcUrl);
				BufferedImage jpg = ImageIO.read(imgUrl);
				if (jpg == null) {
					// saveFilePath = srcUrl.toString();
					continue;
				}
				// Files.createFile(filePath);
				ImageIO.write(jpg, "jpg", new java.io.File(filePath));

				saveFilePath = filePath.substring(filePath.indexOf("upload") - 1);
				break;
			} catch (Exception e) {
				// saveFilePath = srcUrl.toString();
				if (http.isEmpty()) {
					break;
				}
			}
		}
		if (!osPath.getOS().contains("win")) {
			try {
				Runtime.getRuntime().exec("chmod -R 755 " + uploadPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (saveFilePath != null) {
			saveFilePath = saveFilePath.replace("\\", "/");
		}

		return saveFilePath;
	}
}