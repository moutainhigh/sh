/**
@Name : chenwei
@Time : 2019/12/15 11:44 上午
**/
package main

import (
	"dragon_ship_api/component/config"
	"flag"
)

var env string

func main() {
	flag.StringVar(&env, "e", "test", "environment")
	flag.Parse()
	config.Load(env)
}
