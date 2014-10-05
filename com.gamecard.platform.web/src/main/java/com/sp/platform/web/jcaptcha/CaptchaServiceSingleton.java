package com.sp.platform.web.jcaptcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.SimpleTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class CaptchaServiceSingleton extends ListImageCaptchaEngine {
    private static Logger log = Logger.getLogger(CaptchaServiceSingleton.class);

    private static final Integer MIN_WORD_LENGTH = new Integer(4);

    private static final Integer MAX_WORD_LENGTH = new Integer(4);

    private static final Integer IMAGE_WIDTH = new Integer(90);

    private static final Integer IMAGE_HEIGHT = new Integer(30);

    private static final Integer MIN_FONT_SIZE = new Integer(12);

    private static final Integer MAX_FONT_SIZE = new Integer(14);

    private static final String NUMERIC_CHARS = "23456789";// No numeric 0

    private static final String UPPER_ASCII_CHARS = "ABCDEFGHJKLMNPQRSTXYZ";// No

    // upper
    // O I

    // we dont use the lower characters because it cause dificult in some case,
    // so that
    // we must always UPPERCASE the input from user (currently in
    // OnlineUserImpl)
    private static final String LOWER_ASCII_CHARS = "abcdefghjkmnpqrstxyz";// No

    // lower
    // o i

    /**
     * Singleton instance of this class
     */
    private static CaptchaServiceSingleton instance = new CaptchaServiceSingleton();

    private ArrayList textPasterList;

    private ArrayList backgroundGeneratorList;

    private ArrayList fontGeneratorList;

    ImageCaptcha imageCaptcha = null;

    /**
     * Private constructor to prevent instantiation
     */
    private CaptchaServiceSingleton() {
    }

    public static CaptchaServiceSingleton getInstance() {
        return instance;
    }

    protected void buildInitialFactories() {
        try {
            textPasterList = new ArrayList();
            backgroundGeneratorList = new ArrayList();
            fontGeneratorList = new ArrayList();

            textPasterList.add(new SimpleTextPaster(MIN_WORD_LENGTH,
                    MAX_WORD_LENGTH, Color.black));
            textPasterList.add(new RandomTextPaster(MIN_WORD_LENGTH,
                    MAX_WORD_LENGTH, Color.black));
            textPasterList.add(new SimpleTextPaster(MIN_WORD_LENGTH,
                    MAX_WORD_LENGTH, Color.black));
            textPasterList.add(new RandomTextPaster(MIN_WORD_LENGTH,
                    MAX_WORD_LENGTH, Color.black));
            textPasterList.add(new SimpleTextPaster(MIN_WORD_LENGTH,
                    MAX_WORD_LENGTH, Color.blue));
            textPasterList.add(new RandomTextPaster(MIN_WORD_LENGTH,
                    MAX_WORD_LENGTH, Color.blue));

            backgroundGeneratorList.add(new GradientBackgroundGenerator(
                    IMAGE_WIDTH, IMAGE_HEIGHT, Color.orange, Color.white));
            backgroundGeneratorList.add(new GradientBackgroundGenerator(
                    IMAGE_WIDTH, IMAGE_HEIGHT, Color.white, Color.green));
            backgroundGeneratorList.add(new GradientBackgroundGenerator(
                    IMAGE_WIDTH, IMAGE_HEIGHT, Color.gray, Color.white));
            backgroundGeneratorList.add(new GradientBackgroundGenerator(
                    IMAGE_WIDTH, IMAGE_HEIGHT, Color.white, Color.orange));
            backgroundGeneratorList.add(new GradientBackgroundGenerator(
                    IMAGE_WIDTH, IMAGE_HEIGHT, Color.green, Color.white));
            backgroundGeneratorList.add(new GradientBackgroundGenerator(
                    IMAGE_WIDTH, IMAGE_HEIGHT, Color.yellow, Color.white));
            backgroundGeneratorList.add(new GradientBackgroundGenerator(
                    IMAGE_WIDTH, IMAGE_HEIGHT, Color.white, Color.magenta));

            Font f = Font.decode("华文细黑");
            fontGeneratorList.add(new RandomFontGenerator(MIN_FONT_SIZE,
                    MAX_FONT_SIZE, new Font[]{f}));// to easy to read
            WordGenerator words = new RandomWordGenerator(NUMERIC_CHARS
                    + UPPER_ASCII_CHARS);

            for (Iterator fontIter = fontGeneratorList.iterator(); fontIter
                    .hasNext(); ) {
                FontGenerator font = (FontGenerator) fontIter.next();

                for (Iterator backIter = backgroundGeneratorList.iterator(); backIter
                        .hasNext(); ) {
                    BackgroundGenerator back = (BackgroundGenerator) backIter
                            .next();
                    for (Iterator textIter = textPasterList.iterator(); textIter
                            .hasNext(); ) {
                        TextPaster parser = (TextPaster) textIter.next();

                        WordToImage word2image = new ComposedWordToImage(font,
                                back, parser);
                        ImageCaptchaFactory factory = new GimpyFactory(words,
                                word2image);
                        addFactory(factory);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("产生校验码初始化异常 ＝＝ " + ex);
            // throw ex;
        }
    }

    /**
     * Write the captcha image of current user to the servlet response
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException
     */
    public void writeCaptchaImage(HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {

        imageCaptcha = getNextImageCaptcha();
        HttpSession session = request.getSession();
        session.setAttribute("imageCaptcha", imageCaptcha);
        BufferedImage image = (BufferedImage) imageCaptcha.getChallenge();

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            // render the captcha challenge as a JPEG image in the response
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            response.setContentType("image/jpeg");

            JPEGImageEncoder encoder = JPEGCodec
                    .createJPEGEncoder(outputStream);
            encoder.encode(image);

            outputStream.flush();
            outputStream.close();
            outputStream = null;// no close twice
        } catch (IOException ex) {
            log.error("产生图片异常 ＝＝ " + ex);
            throw ex;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                }
            }
            imageCaptcha.disposeChallenge();
        }
    }

    public boolean validateCaptchaResponse(String validateCode,
                                           HttpSession session) {
        boolean flag = true;
        try {
            imageCaptcha = (ImageCaptcha) session.getAttribute("imageCaptcha");
            if (imageCaptcha == null) {
                flag = false;
            }
            validateCode = validateCode.toUpperCase();// use upper case for
            // easier usage
            flag = (imageCaptcha.validateResponse(validateCode)).booleanValue();
//            session.removeAttribute("imageCaptcha");
            return flag;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("校验码校验异常 ＝＝ " + ex);
            return false;
        }
    }

}  
