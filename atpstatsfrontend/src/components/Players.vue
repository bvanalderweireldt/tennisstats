<template>
  <div>
    <section class="section">
      <div class="container">
        <div class="tile is-ancestor" v-for="player in dobFilter">
            <article class="tile box is-primary notification is-14 is-child">
              <div class="content">
                <p class="title">{{player.name}}</p>
                <p class="subtitle">{{player.dob}}</p>
              </div>
            </article>
        </div>
      </div>
    </section>
    <section class="section is-medium">
      <div class="container">
        <p class="title is-1">ATP Ranking from 1 to 10</p>
        <p class="subtitle is-3">{{ date }}</p>
        <a class="button is-medium" v-on:click="moveBackwardRankingsDate()">Previous Week</a>
        <a class="button is-medium">Next Week</a>
        <table class="table is-fullwidth">
          <thead>
            <tr>
              <td>Ranking</td>
              <td>Name</td>
              <td>Points</td>
              <td>Tournaments played</td>
            </tr>
          </thead>
          <tbody>
            <tr v-for="r in rankingsOneToTen">
              <td>{{ r.ranking }}</td>
              <td>{{ playersMap[r.playerId].name }}</td>
              <td>{{ r.points }}</td>
              <td>{{ r.tournamentPlayed }}</td>            
            </tr>
          </tbody>
        </table>
      </div>
    </section>
    <section class="section">
      <div class="container">
        <input v-model="search" placeholder="Search for player" class="input" type="text">
      </div>
    </section>
    <div class="tile is-ancestor" v-for="i in Math.ceil(players.length / tileSize)">
        <article class="tile box is-primary notification is-3 is-child" v-for="(player, key, index) in searchTextFilter.slice((i - 1) * tileSize, i * tileSize)" v-on:click="displayMatches(player.id)">
          <div class="content">
            <p class="title">{{player.name}}</p>
            <p class="subtitle">{{player.dob}}</p>
          </div>
        </article>
    </div>
  </div>
</template>

<script>
import moment from 'moment'
import axios from 'axios'
import players from '../assets/players.json'
import matches from '../assets/matches.json'

export default {
  name: 'Players',
  data () {
    return {
      tileSize: 4,
      playersMap: players.reduce(function (map, obj) {
        map[obj.id] = obj
        return map
      }),
      players: players,
      matches: matches,
      rankingsDate: moment().day('Monday'),
      rankings: new Map(),
      rankingsOneToTen: [],
      date: '',
      search: '',
      currentPlayerMatches: null
    }
  },
  methods: {
    displayMatches: function (playerId) {
      this.currentPlayerMatches = this.matches
        .filter(function (match) {
          return match.winner === playerId || match.loser === playerId
        })
    },
    getRankings: function (date) {
      var url = 'https://raw.githubusercontent.com/bvanalderweireldt/tennisstats/master/data/rankings'
      axios.get(url + date.year() + (date.month() + 1) + date.format('DD') + '.json')
        .then(response => {
          this.rankings = response.data.reduce(function (map, obj) {
            map[obj.ranking] = obj
            return map
          })
          for (var i = 1; i <= 10; i++) {
            this.rankingsOneToTen.push(this.rankings[i])
          }
          this.date = this.rankingsDate.format('YYYY MM DD')
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    moveBackwardRankingsDate: function () {
      this.rankingsDate = this.rankingsDate.subtract(7, 'days')
      this.getRankings(this.rankingsDate)
    },
    moveForwardRankingsDate: function () {
      this.rankingsDate = this.rankingsDate.add(7, 'days')
    }
  },
  computed: {
    searchTextFilter: function () {
      var search = this.search
      return this.players
      .filter(function (player) {
        return player.name.toLowerCase().indexOf(search.toLowerCase()) >= 0
      })
      .sort(function (a, b) {
        var nameA = a.name.toUpperCase() // ignore upper and lowercase
        var nameB = b.name.toUpperCase() // ignore upper and lowercase
        if (nameA < nameB) {
          return -1
        }
        if (nameA > nameB) {
          return 1
        }
        // names must be equal
        return 0
      })
    },
    dobFilter: function () {
      return this.players
        .filter(function (player) {
          var dob = moment(player.dob, 'DD/MM/YYYY')
          return (dob.date() === moment().date() && dob.month() === moment().month())
        })
    }
  },
  mounted: function () {
    this.$nextTick(function () {
      this.getRankings(this.rankingsDate)
    })
  }
}
</script>

