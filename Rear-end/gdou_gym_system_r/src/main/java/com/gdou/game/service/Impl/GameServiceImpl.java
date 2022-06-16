package com.gdou.game.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdou.game.domain.Game;
import com.gdou.game.dao.GameMapper;
import com.gdou.game.service.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl extends ServiceImpl<GameMapper, Game> implements IGameService {

    @Autowired
    GameMapper gameMapper;

    /**
     * 更新赛事信息
     * @param game
     * @return
     */
    @Override
    public boolean updateGame(Game game) {
        UpdateWrapper<Game> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("gamename",game.getGamename())
                .set("gametype",game.getGametype())
                .set("gametime",game.getGametime())
                .set("gamelocation",game.getGamelocation())
                .set("gametool",game.getGametool())
                .set("gamenum",game.getGamenum())
                .set("userid",game.getUserid())
        ;
        int flag = gameMapper.update(null, updateWrapper);
        return flag>0;
    }
}
