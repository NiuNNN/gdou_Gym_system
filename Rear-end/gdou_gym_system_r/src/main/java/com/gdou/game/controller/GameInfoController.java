package com.gdou.game.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.gdou.api.CommonResult;
import com.gdou.game.domain.Game;
import com.gdou.game.service.IGameService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping(value = "/game", produces = "application/json")
public class GameInfoController {
    @Autowired
    private  IGameService gameService;

    /**
     * 新建赛事
     * @param game
     * @return CommonResult
     */
    @PostMapping("/save")
    public CommonResult saveGame(@RequestBody Game game){
        gameService.save(game);
        return CommonResult.success();
    }
    /**
     * 取消赛事
     * @param  gameid 器材订单对应编号
     * @return CommonResult
     */
    @DeleteMapping("/delete/{gameid}")
    public CommonResult deletegame(@PathVariable Integer gameid){
        QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("gameid",gameid);
        gameService.remove(queryWrapper);
        return CommonResult.success();
    }

    /**
     * 查询赛事
     */
    @GetMapping("/get")
    public CommonResult getGameInfo(Game game){
        System.out.println(game);
 //       QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("gameid",game.getGameid());
        QueryWrapper<Game> queryWrapper = new QueryWrapper<Game>();
//        Game detail = gameService.getById(game.getGameid());
//        return CommonResult.success(detail);
        queryWrapper.eq("gameid",game.getGameid());
        queryWrapper.like("gamename",game.getGamename());
        queryWrapper.like("gametype",game.getGametype());
        queryWrapper.like("gamelocation",game.getGamelocation());
        queryWrapper.like("gametime",game.getGametime());
        List<Game> list = gameService.list(queryWrapper);
        if(list.size()!=0) return CommonResult.success(list);
        return CommonResult.failed();
    }
    /**
     * 获取id
     */
    @GetMapping("/getid")
    public CommonResult getGameId(Game game){
        long count = gameService.count();
        return CommonResult.success(count);
    }

    /**
     * 获得赛事信息
     */
    @GetMapping("/getgameinfo")
    public  CommonResult getGameinfo(){
        List<Game> list = gameService.list();
        if(list.size() !=0){
            return CommonResult.success(list);
        }else{
            return  CommonResult.failed();
        }
    }

    @GetMapping("/getdaygameinfo")
    public CommonResult getdatgameinfo(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatterd = new SimpleDateFormat("yyyy-MM-dd");
        String now = formatterd.format(calendar.getTime());
        System.out.println(now);
        QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("gametime",now);
        List<Game> list = gameService.list(queryWrapper);
        System.out.println(list);
        if(list.size() !=0){
            return CommonResult.success(list);
        }else{
            return  CommonResult.failed();
        }
    }
}
